package ru.irvindt.prac7;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {
//	private UserRepository userRepository;
//	private JwtTokenProvider jwtTokenProvider;
//
//	@Autowired
//	public SecurityConfig(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
//		this.userRepository = userRepository;
//		this.jwtTokenProvider = jwtTokenProvider;
//	}

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(customizer -> {
//			customizer
//					.requestMatchers("/auth/login").permitAll()
//					.anyRequest().permitAll();
//		});
//		return http.build();
//	}
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
//		http.authorizeRequests()
//				.csrf().disable()
//				.authorizeRequests()
//				.requestMatchers("/auth/check").permitAll()
//				.anyRequest().authenticated();
////				.and()
////				.apply(new JwtConfigurer(JwtFilter()));



//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//				.authorizeRequests()
//					.requestMatchers("/auth/**").permitAll()
//					.anyRequest().authenticated()
//					.and()
//				.apply(new JwtConfigurer(JwtFilter()));
//	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public JwtTokenProvider jwtTokenProvider() {
//		return new JwtTokenProvider();
//	}

//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
}