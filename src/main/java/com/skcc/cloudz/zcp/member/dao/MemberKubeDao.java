package com.skcc.cloudz.zcp.member.dao;

import java.io.IOException;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.internal.LinkedTreeMap;
import com.skcc.cloudz.zcp.common.util.KubeClient;

import ch.qos.logback.classic.Logger;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.ProtoClient.ObjectOrStatus;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ClusterRoleBindingList;
import io.kubernetes.client.models.V1ClusterRoleList;
import io.kubernetes.client.models.V1ServiceAccountList;
import io.kubernetes.client.proto.V1.ServiceAccount;
import io.kubernetes.client.proto.V1.ServiceAccountList;
import io.kubernetes.client.proto.V1Rbac.RoleBinding;
import io.kubernetes.client.util.Config;

@Component
public class MemberKubeDao {

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(MemberKubeDao.class);    
	   
	ApiClient client;// = Configuration.getDefaultApiClient();
	KubeClient<?> api; // = new KubeClient(this.client);
	
	public MemberKubeDao() throws IOException {
		client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);
		api = new KubeClient(this.client);
	}

//	Object test2() throws IOException, ApiException{
//		ApiClient client = Config.defaultClient();
//	    Configuration.setDefaultApiClient(client);
//
//	    ProtoClient pc = new ProtoClient(client);
//	    ObjectOrStatus<PodList> list = pc.list(PodList.newBuilder(), "/api/v1/namespaces/default/pods");
//	    return data;
//	}
//	
//	
//	//쿠버네티스 설정
//	@RequestMapping("/getNamespace")
//	@ResponseBody
//	Object getNamespace() throws IOException, ApiException{
//
//		ApiClient client = Config.defaultClient();
//	    Configuration.setDefaultApiClient(client);
//
//	    ProtoClient pc = new ProtoClient(client);
//	    ObjectOrStatus<NamespaceList> list = pc.list(NamespaceList.newBuilder(), "/api/v1/namespaces");
//
//	    StringBuffer data = new StringBuffer();
//	    if (list.object.getItemsCount() > 0) {
//	    	Namespace p = list.object.getItems(0);
//	      System.out.println(p.toString());
//	      data.append(p.toString());
//	    }
//	    
//	    
//	    return data;
//	}
//	
//	
//	@RequestMapping("/getNode/cpu")
//	@ResponseBody
//	Object getNode() throws IOException, ApiException{
//
//		ApiClient client = Config.defaultClient();
//	    Configuration.setDefaultApiClient(client);
//
//	    ProtoClient pc = new ProtoClient(client);
//	    ObjectOrStatus<NodeList> list = pc.list(NodeList.newBuilder(), "/api/v1/nodes");
//	    String sV = null;
//	    Quantity bb = null;
//	    Map<String, Quantity> value = null;
//	    StringBuffer data = new StringBuffer();
//	    if (list.object.getItemsCount() > 0) {
//	    	Node p = list.object.getItems(0);
//	    	//Map aa = p.getAllFields();
//	    	value = p.getStatus().getCapacityMap();
//	    	bb = value.get("cpu");
//	    	sV = bb.getString();
//	      System.out.println(p.toString());
//	      data.append(p.toString());
//	    }
//	    
//	    
//	    return sV;
//	}
	
	
//	public ClusterRole getClusterRoles() throws IOException, ApiException{
//	    ProtoClient pc = new ProtoClient(client);
//	    ObjectOrStatus<ClusterRoleList > list = pc.list(ClusterRoleList.newBuilder(), "/apis/rbac.authorization.k8s.io/v1/clusterroles");
//	    ClusterRole role = null;
//	    if (list.object.getItemsCount() > 0) {
//	    	role = list.object.getItems(0);
//	    }
//	    return role;
//	}
//
//	public ClusterRoleBinding clusteRroleBindings() throws IOException, ApiException{
//	    ProtoClient pc = new ProtoClient(client);
//	    ObjectOrStatus<ClusterRoleBindingList > list = pc.list(ClusterRoleBindingList.newBuilder(), "/apis/rbac.authorization.k8s.io/v1/clusterrolebindings");
//	    ClusterRoleBinding rolebinding = null;
//	    if (list.object.getItemsCount() > 0) {
//	    	rolebinding = list.object.getItems(0);
//	    }
//	    
//	    return rolebinding;
//	}
	
	public ServiceAccount serviceAccount() throws IOException, ApiException{
	    ProtoClient pc = new ProtoClient(client);
	    CoreV1Api api = new CoreV1Api();
	    
	    ObjectOrStatus<ServiceAccountList > list = pc.list(ServiceAccountList.newBuilder(), "/apis/rbac.authorization.k8s.io/v1/clusterrolebindings");
	    ServiceAccount account = null;
	    if (list.object.getItemsCount() > 0) {
	    	account = list.object.getItems(0);
	    }
	    
	    return account;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedTreeMap clusterRoleList() throws ApiException{
		return (LinkedTreeMap) api.ctlData(() ->{
				ApiResponse<V1ClusterRoleList> data = (ApiResponse<V1ClusterRoleList>) api.apiCall(
						"/apis/rbac.authorization.k8s.io/v1/clusterroles" 
						,null, null, null, null, null, null, null, null, null, null, null);
				Object map = (Object)data.getData();
				LinkedTreeMap mapData = (LinkedTreeMap)map;
				return mapData;
		});
	}
	
	@SuppressWarnings("unchecked")
	public LinkedTreeMap clusterRoleBindingList() throws ApiException{
		return (LinkedTreeMap) api.ctlData(() ->{
			ApiResponse<V1ClusterRoleBindingList> data = (ApiResponse<V1ClusterRoleBindingList>) api.apiCall(
					"/apis/rbac.authorization.k8s.io/v1/clusterrolebindings" 
					,null, null, null, null, null, null, null, null, null, null, null);
			Object map = (Object)data.getData();
			LinkedTreeMap mapData = (LinkedTreeMap)map;
			return mapData;
		});
	}
	
	@SuppressWarnings("unchecked")
	public LinkedTreeMap createRoleBindingList(RoleBinding namespace) throws ApiException{
		return (LinkedTreeMap) api.ctlData(() ->{
			ApiResponse<V1ServiceAccountList> data = (ApiResponse<V1ServiceAccountList>) api.apiCall(
					"/api/v1/namespaces/"+namespace+"/serviceaccounts"
					,null, null, null, null, null, null, null, null, null, null, null);
			Object map = (Object)data.getData();
			LinkedTreeMap mapData = (LinkedTreeMap)map;
			return mapData;
		});
	}
	
	@SuppressWarnings("unchecked")
	public LinkedTreeMap serviceAccountList(String namespace) throws ApiException{
		return (LinkedTreeMap) api.ctlData(() ->{
			ApiResponse<V1ServiceAccountList> data = (ApiResponse<V1ServiceAccountList>) api.apiCall(
					"/api/v1/namespaces/"+namespace+"/serviceaccounts"
					,null, null, null, null, null, null, null, null, null, null, null);
			Object map = (Object)data.getData();
			LinkedTreeMap mapData = (LinkedTreeMap)map;
			return mapData;
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<LinkedTreeMap> createClusterRoleBinding(Object jsonClusterrolebinding) throws ApiException{
		return (List<LinkedTreeMap>) api.ctlData(() ->{
			ApiResponse<V1ClusterRoleBindingList> data = (ApiResponse<V1ClusterRoleBindingList>) api.postApiCall(
					"/apis/rbac.authorization.k8s.io/v1/clusterrolebindings"
					,jsonClusterrolebinding, null, null, null);
			Object map = (Object)data.getData();
			List<LinkedTreeMap> mapData = (List<LinkedTreeMap>)map;
			return mapData;
		});
	}
	
		
}
