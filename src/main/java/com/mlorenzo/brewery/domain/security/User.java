package com.mlorenzo.brewery.domain.security;

import java.util.Set;

import javax.persistence.*;

import com.mlorenzo.brewery.domain.Customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

// Nota: Si un usuario de la aplicación tiene el role Customer, entonces tiene que tener un Customer relacionado

// Usamos las anotaciones @Getter y @Setter en lugar de la anotación @Data porque estamos usando la anotación @ManyToMany y Lombok entra en un loop o bucle infinito y falla
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity {
	private String username;
	private String password;
	
	// Como esta propiedad es una colección, podemos usar la anotaión @Singular de Lombok para añadir al patrón Builder la posibilidad de agregar elementos de uno en uno a la colección.
	// Si no usamos esta anotación, sólo podemos establecer una colección entera mediante el patrón Builder.
	@Singular
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles",
			joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
	private Set<Role> roles;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Customer customer;
	
	// Por defecto, cuando usamos el patrón Builder de Lombok para crear instancias de esta clase, inicializa las propiedades que son objetos a null
	// Entonces, en estas propiedad de abajo usamos la anotación @Builder.Default de Lombok para que el valor true sea el valor por defecto en sus inicializaciones vez de null
	
	@Builder.Default
	private Boolean accountNonExpired = true;
	
	@Builder.Default
	private Boolean accountNonLocked = true;
	
	@Builder.Default
	private Boolean credentialsNonExpired = true;
	
	@Builder.Default
	private Boolean enabled = true;
	
	@Builder.Default
	private Boolean useGoogle2fa = false;
	
	private String google2faSecret;
	
	// Si el usuario se ha registrado con 2FA, esta propiedad indica, si es true, que requiere la verificación del código 2FA para completar su proceso de login, es decir, indica que
	// requiere la segunda parte de la autenticación con Google Authenticator
	@Transient
	@Builder.Default
    private Boolean google2faRequired = false;
}
