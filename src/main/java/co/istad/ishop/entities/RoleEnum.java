package co.istad.ishop.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

//@Getter
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleEnum {
//    ADMIN(Set.of(PermissionEnum.PRODUCT_WRITE,
//            PermissionEnum.PRODUCT_READ,
//            PermissionEnum.BRAND_WRITE,
//            PermissionEnum.BRAND_READ)),
//    SELL(Set.of(PermissionEnum.PRODUCT_READ));
//
//    private final Set<PermissionEnum>  permissions;
//
//    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
//        Set<SimpleGrantedAuthority> authorities = this.permissions.stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getDescription()))
//                .collect(Collectors.toSet());
//
//        // Add role as authority (e.g., ROLE_ADMIN)
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//
//        return authorities;
//    }

}
