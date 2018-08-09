package common;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kitty.springcloud.common.utils.HttpClientUtils;
import com.kitty.springcloud.common.utils.JsonUtils;

public class TestOauthClient {

	private String accessTokenUrl = "http://localhost:9999/oauth/token";

	private String access_token;

	private String refresh_token;

	@Before
	public void getAccessToken() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", "webclient");
		params.put("client_secret", "client_secret");
		params.put("grant_type", "password");
		params.put("response_type", "token");
		params.put("username", "admin");
		params.put("password", "123456");
		String result = HttpClientUtils.post(accessTokenUrl, params);
		Map<String, Object> resultMap = JsonUtils.toObject(result, new TypeReference<Map<String, Object>>() {
		});
		access_token = String.valueOf(resultMap.get("access_token"));
		refresh_token = String.valueOf(resultMap.get("refresh_token"));
		System.out.println("access_token=" + access_token + "----refresh_token=" + refresh_token);

	}

//	@Test
//	public void testCreateToken() {
//
//		String getUserInfoUrl = "http://localhost:9999/users/user?access_token=" + access_token;
//		String result = HttpClientUtils.get(getUserInfoUrl);
//		System.out.println(result);
//	}
//
//	@Test
//	public void testRefreshToken() {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("client_id", "webclient");
//		params.put("client_secret", "client_secret");
//		params.put("grant_type", "refresh_token");
//		params.put("response_type", "token");
//		params.put("refresh_token", refresh_token);
//		String result = HttpClientUtils.post(accessTokenUrl, params);
//		System.out.println(result);
//	}
}
