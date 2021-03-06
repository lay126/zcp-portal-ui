package com.skcc.cloudz.zcp.portal.alert.channels.service;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.skcc.cloudz.zcp.common.util.Message;
import com.skcc.cloudz.zcp.portal.alert.channels.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.portal.alert.channels.vo.ChannelVo;

@Service
public class ChannelService {

	private static Logger logger = Logger.getLogger(ChannelService.class);

	@Value("${props.alertmanager.baseUrl}")
	private String baseUrl;

	@Value("${props.iam.baseUrl}")
	private String iamBaseUrl;

	@Autowired
	Message message;

	public ChannelVo[] getChannelList() {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channel").build().toString();
		logger.info(url);

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelVo[]> entity = new HttpEntity<ChannelVo[]>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelVo[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, ChannelVo[].class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelVo[] channelVo = null;
		if (statusCode == HttpStatus.OK) {
			channelVo = response.getBody();
		}

		return channelVo;
	}

	public ChannelDtlVo getChannelDtl(String id) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channel/{id}").buildAndExpand(id).toString();
		logger.info(url);

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelDtlVo> entity = new HttpEntity<ChannelDtlVo>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelDtlVo> response = restTemplate.exchange(url, HttpMethod.GET, entity, ChannelDtlVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelDtlVo channelDtlVo = null;
		if (statusCode == HttpStatus.OK) {
			channelDtlVo = response.getBody();
			if (channelDtlVo.getEmail_to() != null) {
				if (channelDtlVo.getEmail_to().contains(",")) {
					channelDtlVo.setEmail_to(channelDtlVo.getEmail_to().replaceAll(",", ";"));
				}
			}
		}

		return channelDtlVo;
	}

	public ChannelVo createChannel(Map<String, Object> params) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channel").build().toString();
		logger.info(url);

		ChannelVo channelParam = new ChannelVo();
		channelParam.setChannel(params.get("newChannel").toString());

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelVo> entity = new HttpEntity<ChannelVo>(channelParam, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelVo> response = restTemplate.exchange(url, HttpMethod.POST, entity, ChannelVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelVo channelVo = null;
		if (statusCode == HttpStatus.OK) {
			channelVo = response.getBody();
		}

		return channelVo;
	}

	public ChannelVo deleteChannel(ChannelVo params) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channel/{id}").buildAndExpand(params.getId())
				.toString();
		logger.info(url);

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelVo> entity = new HttpEntity<ChannelVo>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelVo> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, ChannelVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelVo channelVo = null;
		if (statusCode == HttpStatus.OK) {
			channelVo = response.getBody();
		}

		return channelVo;
	}

	public ChannelDtlVo updateChannel(Map<String, Object> params) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channel/{id}").buildAndExpand(params.get("id"))
				.toString();
		logger.info(url);

		ChannelDtlVo channelParam = new ChannelDtlVo();

		channelParam.setChannel(params.get("channel").toString());
		
		if (params.get("pEmail_to") != null) {
			String[] emails = params.get("pEmail_to").toString().split(";");
			String email_to = "";
			for (String email : emails) {
				if (!"".equals(email)) {
					if ("".equals(email_to)) {
						email_to = email_to.concat(email.replaceAll(";", ""));
					} else {
						email_to = email_to.concat("," + email.replaceAll(";", ""));
					}
				}
			}
			channelParam.setEmail_to(email_to);
		}

		channelParam.setSlack_api_url(params.get("pSlack_api_url").toString());

		channelParam.setHipchat_api_url(params.get("pHipchat_api_url").toString());
		channelParam.setHipchat_room_id(params.get("pHipchat_room_id").toString());
		channelParam.setHipchat_auth_token(params.get("pHipchat_auth_token").toString());
		channelParam.setHipchat_notify(params.get("pHipchat_notify").toString());

		channelParam.setWebhook_url(params.get("pWebhook_url").toString());

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelDtlVo> entity = new HttpEntity<ChannelDtlVo>(channelParam, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelDtlVo> response = restTemplate.exchange(url, HttpMethod.PUT, entity, ChannelDtlVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelDtlVo channelDtlVo = null;
		if (statusCode == HttpStatus.OK) {
			channelDtlVo = response.getBody();
		}

		return channelDtlVo;
	}

	public ChannelDtlVo updateChannelDtl(Map<String, Object> params) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channel/{id}").buildAndExpand(params.get("id"))
				.toString();
		logger.info(url);

		ChannelDtlVo channelParam = new ChannelDtlVo();

		channelParam.setChannel(params.get("channel").toString());

		if (params.get("email_to") != null) {
			String[] emails = params.get("email_to").toString().split(";");
			String email_to = "";
			for (String email : emails) {
				if (!"".equals(email)) {
					if ("".equals(email_to)) {
						email_to = email_to.concat(email.replaceAll(";", ""));
					} else {
						email_to = email_to.concat("," + email.replaceAll(";", ""));
					}
				}
			}
			channelParam.setEmail_to(email_to);
		}

		if (params.get("slack_api_url") != null) {
			channelParam.setSlack_api_url(params.get("slack_api_url").toString());
		}
		if (params.get("hipchat_api_url") != null) {
			channelParam.setHipchat_api_url(params.get("hipchat_api_url").toString());
			channelParam.setHipchat_room_id(params.get("hipchat_room_id").toString());
			channelParam.setHipchat_auth_token(params.get("hipchat_auth_token").toString());
			channelParam.setHipchat_notify(params.get("hipchat_notify").toString());
		}
		if (params.get("webhook_url") != null) {
			channelParam.setWebhook_url(params.get("webhook_url").toString());
		}

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelDtlVo> entity = new HttpEntity<ChannelDtlVo>(channelParam, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelDtlVo> response = restTemplate.exchange(url, HttpMethod.PUT, entity, ChannelDtlVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelDtlVo channelDtlVo = null;
		if (statusCode == HttpStatus.OK) {
			channelDtlVo = response.getBody();
		}

		return channelDtlVo;
	}

	public ChannelVo updateChannelName(Map<String, Object> params) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/channelName/{id}")
				.buildAndExpand(params.get("id")).toString();
		logger.info(url);

		ChannelVo channelParam = new ChannelVo();
		channelParam.setChannel(params.get("channel").toString());

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelVo> entity = new HttpEntity<ChannelVo>(channelParam, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelVo> response = restTemplate.exchange(url, HttpMethod.PUT, entity, ChannelVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelVo channelVo = null;
		if (statusCode == HttpStatus.OK) {
			channelVo = response.getBody();
		}

		return channelVo;
	}

	public ChannelDtlVo deleteNotification(Map<String, Object> params) {
		String url = UriComponentsBuilder.fromUriString(baseUrl).path("/notification/{id}")
				.buildAndExpand(params.get("id")).toString();
		logger.info(url);

		ChannelDtlVo channelParam = new ChannelDtlVo();

		if (params.get("email_to") != null) {
			channelParam.setEmail_to(params.get("email_to").toString());
		}
		if (params.get("slack_api_url") != null) {
			channelParam.setSlack_api_url(params.get("slack_api_url").toString());
		}
		if (params.get("hipchat_api_url") != null) {
			channelParam.setHipchat_api_url(params.get("hipchat_api_url").toString());
		}
		if (params.get("webhook_url") != null) {
			channelParam.setWebhook_url(params.get("webhook_url").toString());
		}

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChannelDtlVo> entity = new HttpEntity<ChannelDtlVo>(channelParam, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChannelDtlVo> response = restTemplate.exchange(url, HttpMethod.DELETE, entity,
				ChannelDtlVo.class);

		HttpStatus statusCode = response.getStatusCode();

		ChannelDtlVo channelDtlVo = null;
		if (statusCode == HttpStatus.OK) {
			channelDtlVo = response.getBody();
		}

		return channelDtlVo;
	}

}
