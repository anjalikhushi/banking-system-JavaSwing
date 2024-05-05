/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.learn.mycartt.servlets;

import com.learn.mycartt.dao.CategoryDao;
import com.learn.mycartt.dao.ProductDao;
import com.learn.mycartt.entities.Category;
import com.learn.mycartt.entities.Product;
import com.learn.mycartt.helper.FactoryProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import static net.bytebuddy.matcher.ElementMatchers.is;

/**
 *
 * @author HP
 */@MultipartConfig
public class ProductOperationServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //fetching category data
            
            
            
            String op=request.getParameter("operation");
            
            if(op.trim().equals("addcategory")){
                String title = request.getParameter("catTitle");
                String description=request.getParameter("catDescription");
                
                Category category = new Category();
                category.setCategoryTitle(title);
                category.setCategoryDescription(description);
                
                
                
                //category database save
                CategoryDao categoryDao=new CategoryDao(FactoryProvider.getFactory());
                
                int catId=categoryDao.saveCategory(category);
                //out.println("category saved");
                HttpSession httpSession=request.getSession();
                httpSession.setAttribute("message","Category added successfully : "+ catId);
                response.sendRedirect("admin.jsp");
                return;
                
                
                
                
            }else if(op.trim().equals("addproduct")){
               
                String pName = request.getParameter("pName");
                String pDesc = request.getParameter("pDesc");
                int pPrice=Integer.parseInt(request.getParameter("pPrice"));
                int pDiscount=Integer.parseInt(request.getParameter("pDiscount"));
                int pQuantity=Integer.parseInt(request.getParameter("pQuantity"));
                int catId=Integer.parseInt(request.getParameter("catId"));
                Part part=request.getPart("pPic");
                
                Product p=new Product();
                p.setpName(pName);
                 p.setpDesc(pDesc);
                  p.setpPrice(pPrice);
                   p.setpDiscount(pDiscount);
                    p.setpQuantity(pQuantity);
                     p.setpPhoto(part.getSubmittedFileName());
                
                     CategoryDao cdao=new CategoryDao(FactoryProvider.getFactory());
                     Category category=cdao.getCategoryById(catId);
                p.setCategory(category);
                
                ProductDao pdao=new ProductDao(FactoryProvider.getFactory());
                pdao.saveProduct(p);
                
                //pic upload
                String path=request.getRealPath("img")+File.separator+"products"+File.separator+part.getSubmittedFileName();
                System.out.println(path);
                
               
                try{
                FileOutputStream fos = new FileOutputStream(path);
                //FileInputStream fis=new FileInputStream(part.getInputStream());
                InputStream is=part.getInputStream();
                byte []data=new byte[is.available()];
                is.read(data);
                fos.write(data);
                fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                
                //out.println("Product save success....");
                HttpSession httpSession=request.getSession();
                httpSession.setAttribute("message","Product added successfully .... ");
                response.sendRedirect("admin.jsp");
                return;
                
            }
            
            
            
            
            
            
            
            
            
            
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}