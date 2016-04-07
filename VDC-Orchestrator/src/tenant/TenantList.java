package tenant;

import java.util.List;

public class TenantList {
	private List<String> tenants_link;
	private List<Tenant> tenants;
	
	public TenantList(List<String> tenants_link, List<Tenant> tenants){
		this.tenants_link = tenants_link;
		this.tenants = tenants;
	}
	
	public List<String> getTenantsLink(){
		return this.tenants_link;
	}
	
	public List<Tenant> getTenants(){
		return this.tenants;
	}
}
