package afamijas;

import afamijas.dao.PostalcodesRepository;
import afamijas.dao.RouteStopsRepository;
import afamijas.dao.RoutesRepository;
import afamijas.model.PostalCode;
import afamijas.model.Route;
import afamijas.model.RouteStop;
import afamijas.model.User;
import afamijas.security.JwtFilter;
import afamijas.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@EnableScheduling
@SpringBootApplication
public class AfamijasApplication extends SpringBootServletInitializer implements ApplicationRunner
{

	//TODO: DESHABILITAR ESTO CUANDO TODA LA CARGA INCIAL ESTÉ COMPLETA
	@Autowired
	private UsersService usersService;

	@Autowired
	private RoutesRepository routesRepository;

	@Autowired
	private RouteStopsRepository routeStopsRepository;

	@Autowired
	private PostalcodesRepository postalcodesRepository;



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

		/* FIRST RUN
    	User admin = this.usersService.findByUsername("rootafa");
		if(admin==null)
		{
			User user = new User();
			user.setUsername("rootafa");
			user.setPassword(new BCryptPasswordEncoder().encode("AFA_2018"));
			user.setEmail("rpastor@uma.es");
			user.setRoles((Arrays.asList(new String[]{"ROOT", "RELATIVE", "TRANSPORT"})));
			user.setStatus("A");
			user.setToken(UUID.randomUUID().toString());

			this.usersService.save(user);
		}
		/* FIN FIRST RUN */


		/* CARGA DE RUTAS *

		Route route = new Route("RUTA A");
		route = this.routesRepository.save(route);

		RouteStop routeStop = new RouteStop(route.get_id());
		routeStop.setName("CALAHONDA");
		routeStop.setOrder(1);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("CALA DE MIJAS");
		routeStop.setOrder(2);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("CALLE SANTA MARÍA (LAS LAGUNAS)");
		routeStop.setOrder(3);
		this.routeStopsRepository.save(routeStop);


		route = new Route("RUTA B");
		route = this.routesRepository.save(route);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("FARMACIA CAMINO VIEJO DE COÍN");
		routeStop.setOrder(1);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("BP AJOLI");
		routeStop.setOrder(2);
		this.routeStopsRepository.save(routeStop);



		route = new Route("RUTA C");
		route = this.routesRepository.save(route);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("ROTONDA GUARDIA CIVIL");
		routeStop.setOrder(1);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("ROTONDA FORD");
		routeStop.setOrder(2);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("FARMACIA CARRETERA DE MIJAS");
		routeStop.setOrder(3);
		this.routeStopsRepository.save(routeStop);



		route = new Route("RUTA D");
		route = this.routesRepository.save(route);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("CASA MUSEO");
		routeStop.setOrder(1);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("BAR MIRLO");
		routeStop.setOrder(2);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("POSADA");
		routeStop.setOrder(3);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("FRAGUA");
		routeStop.setOrder(4);
		this.routeStopsRepository.save(routeStop);

		routeStop = new RouteStop(route.get_id());
		routeStop.setName("BAR NIÑO");
		routeStop.setOrder(5);
		this.routeStopsRepository.save(routeStop);

		*/


		/* FIN CARGA DE RUTAS */



		/* CORRECCIÓN DE CÓDIGOS POSTALES

		List<PostalCode> postalCodeList = this.postalcodesRepository.findAll();
		for(PostalCode pc : postalCodeList)
		{
			if(pc.getCode().length()==4)
			{
				pc.setCode("0" + pc.getCode());
				this.postalcodesRepository.save(pc);
			}
		}



		/* FIN CORRECCIÓN DE CÓDIGOS POSTALES */

	}


}