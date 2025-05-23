package net.maku.framework.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.maku.framework.common.constant.CustomerHeader;
import net.maku.framework.security.cache.TokenStoreCache;
import net.maku.framework.security.user.UserDetail;
import net.maku.framework.security.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * 认证过滤器
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Component
@AllArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final TokenStoreCache tokenStoreCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        String header = request.getHeader(CustomerHeader.IDENTITY_HEADER);
//        if (CustomerHeader.DEVICE_IDENTITY_HEADER_VALUE.equals(header)) {
//            UserDetail adminUser = new UserDetail();
//            adminUser.setId(1L);
//            adminUser.setSuperAdmin(10000);
//            adminUser.setUsername("admin");
//            adminUser.setAuthoritySet(Set.of("*"));
//            auth(adminUser);
//            chain.doFilter(request, response);
//            return;
//        }
        String accessToken = TokenUtils.getAccessToken(request);
        // accessToken为空，表示未登录
        if (StringUtils.isBlank(accessToken)) {
            chain.doFilter(request, response);
            return;
        }

        // 获取登录用户信息
        UserDetail user = tokenStoreCache.getUser(accessToken);
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }
        auth(user);
        chain.doFilter(request, response);
    }

    private void auth(UserDetail user) {
        // 用户存在
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // 新建 SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
