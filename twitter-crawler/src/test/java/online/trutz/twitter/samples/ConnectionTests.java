package online.trutz.twitter.samples;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import online.trutz.twitter.samples.model.Politiker;
import online.trutz.twitter.samples.model.User;
import online.trutz.twitter.samples.services.PolitikerRepository;
import online.trutz.twitter.samples.services.TweetRepository;
import online.trutz.twitter.samples.services.TwitterService;

@SpringBootTest
class ConnectionTests {

    private static final Logger log = LoggerFactory.getLogger(ConnectionTests.class);

    @Autowired
    TwitterService twitterService;
    @Autowired
    PolitikerRepository politikerRepository;
    @Autowired
    TweetRepository tweetRepository;

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = { "Alice_Weidel", "Tino_Chrupalla", "film_kunst", "jafuernrw", "KraZMagazin",
            "hallofraukaiser", "Wagner_AfD_MdL",
            "leisten_anna", "JA_Deutschland", "Hannes_Gnauck", "SvenKachelmann", "TomaszFroelich",
            "HartwigJa", "LobstedtJochen", "ManuelWurm", "EngelhEric" })
    void importRechtePolitiker(String twitterName) {
        importPolitiker(twitterName, "rechts");
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = { "ABaerbock", "Janine_Wissler", "schirdewan" })
    void importLinkePolitiker(String twitterName) {
        importPolitiker(twitterName, "links");
    }

    @ParameterizedTest
    @ValueSource(strings = { "ABaerbock"})
    void importLinkePolitikerTest(String twitterName) {
        importPolitiker(twitterName, "links");
    }

    private void importPolitiker(String twitterName, String haltung) {
        User user = twitterService.usersByUsername(twitterName);
        Politiker politiker = new Politiker(user.id(), user.name(), user.username(), haltung);
        politikerRepository.save(politiker);
        log.info("{}", politiker);
        tweetRepository.saveAll(twitterService.tweets(politiker.id()).toList());
    }

}
