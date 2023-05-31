package info.bliki.api;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import software.betamax.ConfigurationBuilder;
import software.betamax.junit.Betamax;
import software.betamax.junit.RecorderRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;
import static software.betamax.TapeMode.READ_ONLY;
import static software.betamax.TapeMode.READ_SEQUENTIAL;

public class ConnectorTest {
    private Connector subject;

    @Rule public RecorderRule recorder = new RecorderRule(new ConfigurationBuilder().sslEnabled(true).build());
    private User anonUser;

    @Before public void before() {
        subject = new TestUser.TestConnector();
        anonUser = new User(null, null, "https://en.wiktionary.org/w/api.php");
        initMocks(this);
    }

    @Test public void testLoginAsAnonymous() {
        User user = new User(null, null, null);
        User result = subject.login(user);
        assertThat(result).isSameAs(user);
    }

    @Betamax(tape="loginWithUsernameFailed", mode = READ_SEQUENTIAL)
    @Test public void testLoginWithUsernameFailure() throws Exception {
        User user = new User("someUser", "somePassword", "https://meta.wikimedia.org/w/api.php");
        User result = subject.login(user);
        assertThat(result).isNull();
    }

    @Ignore
    @Betamax(tape = "loginWithUsernameSuccess", mode = READ_SEQUENTIAL)
    @Test public void testLoginWithUsernameSuccess() throws Exception {
        User user = new User("jberkel", "testing", "https://en.wiktionary.org/w/api.php");
        User result = subject.login(user);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jberkel");
        assertThat(result.getNormalizedUsername()).isEqualTo("Jberkel");
        assertThat(result.getUserid()).isEqualTo("1580588");
    }

    @Ignore
    @Betamax(tape = "queryContentFoo", mode = READ_ONLY)
    @Test public void testQueryContent() throws Exception {
        List<Page> pages = subject.queryContent(anonUser, Collections.singletonList("foo"));
        assertThat(pages).hasSize(1);

        Page page = pages.get(0);

        assertThat(page.getTitle()).isEqualTo("foo");
        assertThat(page.getCurrentContent()).isNotEmpty();
        assertThat(page.sizeOfCategoryList()).isZero();
        assertThat(page.getPageid()).isEqualTo("39480");
    }
}
