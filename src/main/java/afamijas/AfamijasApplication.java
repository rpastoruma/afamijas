package afamijas;

import afamijas.model.User;
import afamijas.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import afamijas.security.JwtFilter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class AfamijasApplication extends SpringBootServletInitializer implements ApplicationRunner
{

	//* FIRST RUN
	@Autowired
	private UsersService usersService;
    //*/

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(AfamijasApplication.class);
	}

	public static void main(String[] args) throws Exception
	{
		SpringApplication.run(AfamijasApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<JwtFilter> corsFilter()
	{
		final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<JwtFilter>();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/private/*");

		return registrationBean;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{

		//* FIRST RUN
    	User admin = this.usersService.findByUsername("rootAFA");
		if(admin==null)
		{
			User user = new User();
			user.setUsername("rootAFA");
			user.setPassword(new BCryptPasswordEncoder().encode("AFA_2018"));
			user.setEmail("sicuma.test@gmail.com");
			user.setRole("admin");
			user.setStatus("A");

			this.usersService.save(user);
		}
		 //*/
	}


}