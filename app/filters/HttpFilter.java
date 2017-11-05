package filters;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.google.inject.Inject;

import akka.stream.Materializer;
import controllers.routes;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.routing.Router;

public class HttpFilter implements HttpFilters{
	private final EssentialFilter[] filters;
	
	@Inject
	public HttpFilter(SecurityFilter securityFilter){
		filters = new EssentialFilter[]{securityFilter};
	}

	@Override
	public EssentialFilter[] filters() {
		
		return filters;
	}
}

class SecurityFilter extends Filter{
	@Inject
	public SecurityFilter(Materializer mat) {
		super(mat);
	}

	boolean needAuth(RequestHeader requestHeader){
		
		return true;
	}
	@Override
	public CompletionStage<Result> apply(Function<RequestHeader, CompletionStage<Result>> next, RequestHeader requestHeader) {
		String loginUrl = controllers.routes.HomeController.login().url();
		System.out.println("SecurityFilter.apply() requestHeader.uri():"+requestHeader.uri().toString()+" loginUrl:"+loginUrl);
		
		
		
		return next.apply(requestHeader);
	}
	
}
