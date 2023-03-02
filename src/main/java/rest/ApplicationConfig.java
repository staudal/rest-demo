package rest;

import filters.CorsFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.servlet.*;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application implements Filter {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(exceptions.GenericExceptionMapper.class);
        resources.add(exceptions.PersonNotFoundExceptionMapper.class);
        resources.add(exceptions.MissingInputExceptionMapper.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        resources.add(PersonResource.class);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Register the CorsFilter
        FilterRegistration.Dynamic corsFilter = filterConfig.getServletContext().addFilter("CorsFilter", CorsFilter.class);
        corsFilter.setInitParameters(Collections.singletonMap("cors.support.credentials", "true"));
        corsFilter.addMappingForUrlPatterns(null, false, "/*");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
