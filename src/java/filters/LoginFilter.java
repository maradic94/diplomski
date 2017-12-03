/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import entity.Admin;
import entity.SessionEntity;
import entity.Student;
import entity.Teacher;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Korisnik
 */

public class LoginFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SessionEntity sessionEntity = (SessionEntity) ((HttpServletRequest) request).getSession().getAttribute("sessionEntity");

        if (sessionEntity == null) {
            chain.doFilter(request, response);
            return;
        } else {
            if (sessionEntity.getMyUser() == null) {
                chain.doFilter(request, response);
                return;
            }
        }

        if (sessionEntity.getMyUser() instanceof Student) {
            ((HttpServletResponse) response).sendRedirect("system/student_home.xhtml");
        } else if (sessionEntity.getMyUser() instanceof Teacher) {
            ((HttpServletResponse) response).sendRedirect("system/teacher_home.xhtml");
        } else if (sessionEntity.getMyUser() instanceof Admin) {
            ((HttpServletResponse) response).sendRedirect("system/admin_home.xhtml");
        }

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}
