package com.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class EventApplicationTests {
//
//	@Autowired
//	TestRestTemplate restTemplate;
//
//	static List<User> userList;
//
//
//	@BeforeAll
//	static void setup() {
//		userList = Utils.getTestUsers();
//	}
//
//	@AfterAll
//	static void teardown() {
//
//	}
//
//
//	@Test
//	@DirtiesContext
//	void shouldCreateEvent() {
//		EventDto eventDto = EventDto.builder()
//				.name("FirstMeeting")
//				.userid(userList.get(0).getId())
//				.build();
//		ResponseEntity<Void> response = restTemplate
//				.withBasicAuth("amanpreetsingh", "singh")
//				.postForEntity(Constants.CALENDER.CALENDER_EVENT, eventDto, Void.class);
////		ResponseEntity<Void> response = restTemplate
////				.postForEntity("/calender/event", eventDto, Void.class);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//	}
//
//  @Test
//	void shouldReturnAnEvent() {
//		ResponseEntity<EventDto> response = restTemplate
//				.withBasicAuth("amanpreetsingh", "singh")
//				.getForEntity(Constants.CALENDER.CALENDER_EVENT + "/53", EventDto.class);
////		ResponseEntity<EventDto> response = restTemplate
////				.getForEntity("/calender/event/53", EventDto.class);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//		assertThat(response.getBody()).isNotNull();
//		assertThat(response.getBody().getId()).isNotNull();
//		assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(53);
//	}
//
//	@Test
//	void shouldReturnAllEventForRequestingUser() {
//		ResponseEntity<String> response = restTemplate
//				.withBasicAuth("amanpreetsingh", "singh")
//				.getForEntity(Constants.CALENDER.CALENDER_EVENT, String.class);
////		ResponseEntity<EventDto> response = restTemplate
////				.getForEntity("/calender/event/53", EventDto.class);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//		assertThat(response.getBody()).isNotNull();
//		DocumentContext documentContext = JsonPath.parse(response.getBody());
//		int cashCardCount = documentContext.read("$.length()");
//		assertThat(cashCardCount).isEqualTo(4);
//	}
}
