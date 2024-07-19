package sendman.backend.common.mock;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import sendman.backend.account.domain.Account;

import java.util.ArrayList;
import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add((GrantedAuthority) () -> "ROLE_USER");
        Account account = Account.builder().id("10000000000000").email("username@gmail.com").name("username").build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(account, "testPassword", grantedAuthorities);
        context.setAuthentication(authentication);
        return context;
    }
}
