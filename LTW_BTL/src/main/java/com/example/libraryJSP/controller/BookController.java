package com.example.libraryJSP.controller;

import com.example.libraryJSP.DAO.CartDAO;
import com.example.libraryJSP.DAO.StarDAO;
import com.example.libraryJSP.DAO.CommentsDAO;
import com.example.libraryJSP.entities.Account;
import com.example.libraryJSP.entities.Book;
//import com.example.libraryJSP.entities.Review;
import com.example.libraryJSP.service.AccountDao;
import com.example.libraryJSP.service.BookDao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import com.example.libraryJSP.service.ReviewDao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.Part;

@Controller
public class BookController {
    private CommentsDAO commentsDao = new CommentsDAO();
    private CartDAO cartDao = new CartDAO();
    private StarDAO starDao = new StarDAO();
    @InitBinder
    public void initBinder(WebDataBinder data) {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        data.registerCustomEditor(Date.class, new CustomDateEditor(sf,true));
    }
    private SimpleDateFormat sf = new SimpleDateFormat();
    @Autowired
    private BookDao bookdao;
    @Autowired
    private AccountDao accountdao;
    @Autowired
    private MessageSource messageSource;
   


    @RequestMapping("/")
    public String page(HttpSession session, Model model){
//        if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
//            return "redirect:/home";
//        }
//        else{
//            return "redirect:/index";
//        }
        return "redirect:/index";
    }
    @GetMapping("/exportExcel")
    public void exportExcel(HttpSession session,HttpServletResponse response) {
    	if(session.getAttribute("username")==null&&Integer.valueOf((Integer) session.getAttribute("role")) != 1){
            return ;
        }  
        // Tạo workbook mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang trong workbook
        Sheet sheet = workbook.createSheet("Invoice");

        // Tạo một hàng trong trang làm header
        Row headerRow = sheet.createRow(0);

        // Tạo các ô trong hàng đầu tiên
        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("Title");

        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("Author");

        Cell headerCell2 = headerRow.createCell(2);
        headerCell2.setCellValue("Customer");
        
        Cell headerCell3 = headerRow.createCell(3);
        headerCell3.setCellValue("Quantity");
        
        Cell headerCell4 = headerRow.createCell(4);
        headerCell4.setCellValue("TotalPrice");

        // Ghi dữ liệu vào các hàng tiếp theo (dữ liệu mẫu)
        JSONArray carts = cartDao.getListCartAll();
        for (int i = 0; i < carts.length(); i++) {
            JSONObject cart = carts.getJSONObject(i);
            Row dataRow = sheet.createRow(i + 1);

            Cell dataCell0 = dataRow.createCell(0);
            dataCell0.setCellValue(cart.getString("title"));
            
            Cell dataCell1 = dataRow.createCell(1);
            dataCell1.setCellValue(cart.getString("author"));

            Cell dataCell2 = dataRow.createCell(2);
            dataCell2.setCellValue(cart.getString("username"));

            Cell dataCell3 = dataRow.createCell(3);
            dataCell3.setCellValue(cart.getInt("quantity"));
            
            Cell dataCell4 = dataRow.createCell(4);
            dataCell4.setCellValue(cart.getInt("quantity") * cart.getDouble("price"));
        }

        // Thiết lập header cho phản hồi HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=invoice.xlsx");

        // Ghi workbook vào OutputStream của phản hồi
        try {
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
   
    
    }


    @GetMapping("/search{keyword}")
    public String searchBooks(HttpSession session,@RequestParam("keyword") String keyword, Model model) {
    	
        List<Book> books = bookdao.searchBooks(keyword);
        model.addAttribute("books",books);
        model.addAttribute("username",session.getAttribute("username"));
        model.addAttribute("isadmin",session.getAttribute("isadmin"));
        if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
	        if(keyword=="") {
	        	return "redirect:/home";
	        }
	        return "home";
    	}
    	else{
            if(keyword=="") {
            	return "redirect:/index";
            }
            return "index";
        }
    }
    @RequestMapping("/home")
    public String home(HttpSession session, Model model){
        System.out.println(session.getAttribute("role"));
        if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
            List<Book> books = bookdao.getListBook();
            for (Book book : books) {
            	book.setQuantity(bookdao.getQuantity(book.getId()));
            	bookdao.updateBook(book);
            }
            model.addAttribute("books",books);
            model.addAttribute("username",session.getAttribute("username"));
            System.out.println(session.getAttribute("isadmin"));
            model.addAttribute("isadmin",session.getAttribute("isadmin"));
            if(session.getAttribute("delete")!=null){
                model.addAttribute("delete","Successfully deleted!");
            }
            return "home";
        }else{
        	
            return "redirect:/index";
        }
    }
    
    @RequestMapping("/statistical")
    public String statistical(HttpSession session, Model model){
    	if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }
    	else if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
        	 JSONArray carts = cartDao.getListCartAll();
             model.addAttribute("carts", carts);
            model.addAttribute("isadmin",session.getAttribute("isadmin"));
            return "statistical";
        }else{
        	
            return "redirect:/index";
        }
    }
    @RequestMapping("/management")
    public String management(HttpSession session, Model model){
    	if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }
    	else if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
        	 List<Account> accounts = accountdao.getListAccount();
             model.addAttribute("accounts", accounts);
            model.addAttribute("isadmin",session.getAttribute("isadmin"));
            
            return "management";
        }else{
        	
            return "redirect:/index";
        }
    }
    @RequestMapping("/delete/account")
    public String deleteAccount(HttpSession session, @RequestParam("id") Integer id, Model model){
        if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }else {
            accountdao.deleteAcoount(id);
            List<Account> accounts = accountdao.getListAccount();
            model.addAttribute("accounts", accounts);
            model.addAttribute("message", "Delete account successfully");
            return "management";
        }
    }
    @RequestMapping("/sortprice")
    public String sortPrice(Model model, HttpSession session, @RequestParam("id") Integer id) {
    	Sort sort;
    	if(id>0) {	
    		sort = Sort.by(Sort.Direction.ASC, "price");
    	}
    	else {
    		sort = Sort.by(Sort.Direction.	DESC, "price");
    	}
        List<Book> books= bookdao.sortBook(sort);
        model.addAttribute("books",books);
        model.addAttribute("username",session.getAttribute("username"));
        session.setAttribute("id",session.getAttribute("id"));
        if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
        	return "home";
        }
        else return "index";
    }
    @RequestMapping("/index")
    public String index(HttpSession session, Model model){
        List<Book> books = bookdao.getListBook();
        model.addAttribute("books",books);
        model.addAttribute("username",session.getAttribute("username"));
        session.setAttribute("id",session.getAttribute("id"));
//        List<String> bookss;
//        for(Book x:books) {
//        	String photoUrls = HtmlUtils.htmlEscape(x.getPhoto());
//        	bookss.add(photoUrls);
//            model.addAttribute("photoUrls", photoUrls);
//        }
        return "index";
    }
    
    @RequestMapping("/formLogin")
    public String FormLogin(Model model){
        model.addAttribute("account",new Account());
        return "login";
    }
    @RequestMapping("/delete")
    public String deleteBook(HttpSession session, @RequestParam("id") Integer id ){
        if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }else {
            bookdao.deleteBook(id);
            session.setAttribute("delete","Successfully deleted!");
            return "redirect:/";
        }
    }
    @RequestMapping("/update")
    public String updateBook(HttpSession session, @RequestParam("id") Integer id, Model model){
        if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }else if (Integer.valueOf((Integer) session.getAttribute("role")) == 1){
        	if(id>0) {
        		Book book = bookdao.getBook(id);
        		model.addAttribute("book",book);
        		String photoUrls = HtmlUtils.htmlEscape(book.getPhoto());
                model.addAttribute("photoUrls", photoUrls);
        		return "FormUpdate";
        	}
        	else {
        		model.addAttribute("book",new Book());
        		model.addAttribute("check",id);
                model.addAttribute("photoUrls", "0,0");
        		return "FormUpdate";
        	}
            
        }
        else{
            return "redirect:/index";
        }
    }

    @RequestMapping("/detail")
    public String detailBook(HttpSession session, @RequestParam("id") Integer ids, Model model ){
        if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }else {
        	 JSONObject checkstar = starDao.checkCustomer(ids,Integer.valueOf((Integer) session.getAttribute("id")));
             if(checkstar.has("id")) {
          		model.addAttribute("check",0);
             }
             else {
            	 model.addAttribute("check",1);
             }
            Book book = bookdao.getBook(ids);
            JSONArray stars= starDao.getListStar(ids);
            model.addAttribute("book",book);
            model.addAttribute("comments",commentsDao.getListCommentById(ids));
            model.addAttribute("stars",starDao.getListStarById(ids));
            model.addAttribute("username",session.getAttribute("username"));
            session.setAttribute("id",session.getAttribute("id"));
            int totalStar = 0;
            for (int i = 0; i < stars.length(); i++) {
                totalStar += stars.getJSONObject(i).getInt("star");
            }
            int tmp=stars.length();
            if(tmp==0) {
            	tmp=1;
            	totalStar=5;
            }
            model.addAttribute("totalStar", totalStar/tmp);
            return "detail";
        }
    }
    @RequestMapping("/star")
    public String star(Model model, HttpServletRequest request,HttpSession session){ 
        String userName = request.getParameter("userName");
        int idproduct = Integer.valueOf(request.getParameter("idProduct"));
        int star = Integer.valueOf(request.getParameter("star"));
        JSONObject checkstar = starDao.checkCustomer(idproduct,Integer.valueOf((Integer) session.getAttribute("id")));
        if(!checkstar.has("id")) {
        boolean check = starDao.insertStar(star,idproduct,Integer.valueOf((Integer) session.getAttribute("id")),userName);
        if(check == true){
            Book book = bookdao.getBook(idproduct);
            model.addAttribute("book",book);
            model.addAttribute("stars",starDao.getListStarById(idproduct));
            model.addAttribute("comments",commentsDao.getListCommentById(idproduct));
            model.addAttribute("username",session.getAttribute("username"));
     		model.addAttribute("check",0);
     		JSONArray stars= starDao.getListStar(idproduct);
            int totalStar = 0;
            for (int i = 0; i < stars.length(); i++) {
                totalStar += stars.getJSONObject(i).getInt("star");
            }
            int tmp=stars.length();
            if(tmp==0) {
            	tmp=1;
            	totalStar=5;
            }
            model.addAttribute("totalStar", totalStar/tmp);
            session.setAttribute("id",session.getAttribute("id"));
            
            return "detail";
        }
        }
        else {
        	 Book book = bookdao.getBook(idproduct);
             model.addAttribute("book",book);
             model.addAttribute("stars",starDao.getListStarById(idproduct));
             model.addAttribute("comments",commentsDao.getListCommentById(idproduct));
             model.addAttribute("username",session.getAttribute("username"));
     		model.addAttribute("check",0);
     		JSONArray stars= starDao.getListStar(idproduct);
            int totalStar = 0;
            for (int i = 0; i < stars.length(); i++) {
                totalStar += stars.getJSONObject(i).getInt("star");
            }
            int tmp=stars.length();
            if(tmp==0) {
            	tmp=1;
            	totalStar=5;
            }
            model.addAttribute("totalStar", totalStar/tmp);
             session.setAttribute("id",session.getAttribute("id"));
             return "detail";
        }
        return "redirect:/formLogin";
    }
    @RequestMapping("/update1")
    public String updateBook(Model model,HttpSession session,HttpServletRequest request,@Valid @ModelAttribute("book") Book book, BindingResult result)throws ParseException{
    	Book checkDuplicate=bookdao.getBook1(book.getId(),book.getTitle(),book.getAuthor());
    	if(session.getAttribute("username") == null){
            return "redirect:/formLogin";
        }
    	else if(checkDuplicate!=null ) {
    		
    		model.addAttribute("error","Duplicate information");
    		if(book.getId()==0) {
    			model.addAttribute("check",-1);
    			return "FormUpdate";
    		}
    		
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	       try {
	           book.setDatepublic(sdf.parse(request.getParameter("created")));
	       } catch (ParseException e) {
	           throw new RuntimeException(e);
	       }
	            return "FormUpdate";
	    }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                debug date
//                System.out.println(request.getParameter("created"));
            try {
                book.setDatepublic(sdf.parse(request.getParameter("created")));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Book book1 = bookdao.updateBook(book);
            if (book1== null){
                return "redirect:/update";
            }
            String photoUrls = HtmlUtils.htmlEscape(book.getPhoto());
            model.addAttribute("photoUrls", photoUrls);
            return "redirect:/home";
        }
    }

   
    @RequestMapping("/login")
    public String Login(HttpSession session, @Valid @ModelAttribute("account")Account account, BindingResult result, Model model){
            Account acc1 = accountdao.getAccount(account.getUsername());
//            debug session isadmin
//            System.out.println(acc1.toString());
            if(acc1 != null){
                if(acc1.getPassword().equals(account.getPassword())) {
                    session.setAttribute("username", acc1.getUsername());
                    session.setAttribute("isadmin", acc1.getIsadmin());
                    session.setAttribute("id",acc1.getId());
                    session.setAttribute("role",acc1.getIsadmin());
                    if(acc1.getIsadmin()!=1){
                        return "redirect:/index" ;
                    }
                    else {
                        return "redirect:/home";
                    }
                }
                model.addAttribute("account",new Account());
                model.addAttribute("error","Username or Password is incorrect");
                return "login";
            }
            model.addAttribute("account",new Account());
            model.addAttribute("error","Username or Password is incorrect");
            return "login";
//        }
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("username");
        return "redirect:/formLogin";
    }

    @RequestMapping("/registerForm")
    public String RegisterForm(Model model){
        model.addAttribute("account",new Account());
        return "register.html";
    }

    @RequestMapping("/register")
    public String register(Model model,@ModelAttribute("account") Account account){
        Account checkDuplicate=accountdao.getAccount(account.getUsername());
        if (checkDuplicate==null){
//            Default is not admin
            account.setIsadmin(0);
            Account account1 = accountdao.saveAccount(account);
            model.addAttribute("account",new Account());
            model.addAttribute("success","Successfully Registered");
            return "login";
        }
        else{
            model.addAttribute("error","Username is already exists");
            model.addAttribute("account",new Account());
            return "register";
        }

    }
    @RequestMapping("/add/account")
    public String addAccount(Model model,@Valid @ModelAttribute("account") Account account,BindingResult result,HttpSession session){
    	if(session.getAttribute("username")==null){
            return "redirect:/formLogin";
        }
    	else if(Integer.valueOf((Integer) session.getAttribute("role")) == 1) {
    		Account checkDuplicate=accountdao.getAccount(account.getUsername());
        if (checkDuplicate==null){
//            Default is not admin
            account.setIsadmin(account.getIsadmin());
            Account account1 = accountdao.saveAccount(account);
            List<Account> accounts = accountdao.getListAccount();
            model.addAttribute("accounts", accounts);
            model.addAttribute("success","Successfully Registered");
            return "management";
        }
        else{
        	model.addAttribute("error","Username is already exists");
            List<Account> accounts = accountdao.getListAccount();
            model.addAttribute("accounts", accounts);
            return "management";
        }
    	}
    	else return "redirect:/";

    }
   
    @RequestMapping("/comment")
    public String comment(Model model, HttpServletRequest request,HttpSession session){
        String comment = request.getParameter("comment");
        String userName = request.getParameter("userName");
        int idproduct = Integer.valueOf(request.getParameter("idProduct"));
        //System.out.println(Integer.valueOf((Integer) session.getAttribute("id"))+"ache");
        JSONObject checkstar = starDao.checkCustomer(idproduct,Integer.valueOf((Integer) session.getAttribute("id")));
        if(checkstar.has("id")) {
     		model.addAttribute("check",0);
        }
        else {
       	 model.addAttribute("check",1);
        }
        boolean check = commentsDao.insertComment(idproduct,Integer.valueOf((Integer) session.getAttribute("id")),comment,userName);
        if(check == true){
            Book book = bookdao.getBook(idproduct);
            model.addAttribute("book",book);
            //System.out.println(commentsDao.getListCommentById(idproduct).toString());
            model.addAttribute("comments",commentsDao.getListCommentById(idproduct));
            model.addAttribute("stars",starDao.getListStarById(idproduct));
            model.addAttribute("username",session.getAttribute("username"));
            session.setAttribute("id",session.getAttribute("id"));
            JSONArray stars= starDao.getListStar(idproduct);
            int totalStar = 0;
            for (int i = 0; i < stars.length(); i++) {
                totalStar += stars.getJSONObject(i).getInt("star");
            }
            int tmp=stars.length();
            if(tmp==0) {
            	tmp=1;
            	totalStar=5;
            }
            model.addAttribute("totalStar", totalStar/tmp);
            return "detail";
        }
        return "redirect:/formLogin";
    }
    @RequestMapping("/deletecomment")
    public String delComment(Model model,HttpServletRequest request, HttpSession session) {
    	int idProduct = Integer.valueOf(request.getParameter("idProduct"));
        int idComment = Integer.valueOf(request.getParameter("idComment"));
        JSONObject checkstar = starDao.checkCustomer(idProduct,Integer.valueOf((Integer) session.getAttribute("id")));
        if(checkstar.has("id")) {
     		model.addAttribute("check",0);
        }
        else {
       	 model.addAttribute("check",1);
        }
        boolean readDataBase = commentsDao.deleteComment(idComment);
        if (readDataBase) {
        	Book book = bookdao.getBook(idProduct);
            model.addAttribute("book",book);
            model.addAttribute("comments",commentsDao.getListCommentById(idProduct));
            model.addAttribute("stars",starDao.getListStarById(idProduct));
            model.addAttribute("username",session.getAttribute("username"));
            session.setAttribute("id",session.getAttribute("id"));
            JSONArray stars= starDao.getListStar(idProduct);
            int totalStar = 0;
            for (int i = 0; i < stars.length(); i++) {
                totalStar += stars.getJSONObject(i).getInt("star");
            }
            int tmp=stars.length();
            if(tmp==0) {
            	tmp=1;
            	totalStar=5;
            }
            model.addAttribute("totalStar", totalStar/tmp);
            return "detail";
        }
        return "redirect:/formLogin";
    }

    @RequestMapping("/cart")
    public String addCart(Model model, HttpServletRequest request, HttpSession session){
    	
        String quantity = request.getParameter("quantity");
        int idProduct = Integer.valueOf(request.getParameter("idProduct"));
        int idCustomer = Integer.valueOf((Integer) session.getAttribute("id"));
        
        // insert vào cart
        JSONObject readDataBase = cartDao.checkDuplicateBook(idCustomer,idProduct);
        boolean check = false;
        if(readDataBase.has("id")){
            check = cartDao.updateCart(Integer.parseInt(quantity),readDataBase.getInt("quantity"),readDataBase.getInt("id"));
            if(check){
                JSONArray carts = cartDao.getListCartByCustomer(idCustomer);
                model.addAttribute("carts",carts);
                return "redirect:/cartview";
            }
            return "redirect:/formLogin";
        }else{
            check = cartDao.insertCart(Integer.parseInt(quantity),idCustomer,idProduct);
            if(check){
                JSONArray carts = cartDao.getListCartByCustomer(idCustomer);
                model.addAttribute("carts",carts);
                session.setAttribute("id",session.getAttribute("id"));
                return "redirect:/cartview";
            }
            return "redirect:/formLogin";

        }

    }

    @RequestMapping("/deletecart")
    public String delCart(Model model, @RequestParam("id") Integer id, HttpSession session) {
//        int id = Integer.valueOf(request.getParameter("id"));
        boolean readDataBase = cartDao.deleteCart(id);
        if (readDataBase) {

            JSONArray carts = cartDao.getListCartByCustomer(id);
            model.addAttribute("carts", carts);
            session.setAttribute("id", session.getAttribute("id"));
            return "redirect:/cartview";
        }
        return "redirect:/formLogin";
    }
    @RequestMapping("/cartview")
    public String getListCart (Model model,HttpSession session){
        if(session.getAttribute("username")!=null) {
            int idCustomer = Integer.valueOf((Integer) session.getAttribute("id"));
            JSONArray carts = cartDao.getListCartByCustomer(idCustomer);
            model.addAttribute("carts", carts);
            session.setAttribute("id", session.getAttribute("id"));
            int totalQuantity = 0;
            for (int i = 0; i < carts.length(); i++) {
            	totalQuantity += carts.getJSONObject(i).getInt("quantity");
            }
            model.addAttribute("totalQuantity", totalQuantity);
            double totalPrice = 0;
            for (int i = 0; i < carts.length(); i++) {
                totalPrice += carts.getJSONObject(i).getDouble("price") * carts.getJSONObject(i).getInt("quantity");
            }
            model.addAttribute("totalPrice", totalPrice);
            return "cart";
        }
        else{
            return "redirect:/index";
        }
    }



}
