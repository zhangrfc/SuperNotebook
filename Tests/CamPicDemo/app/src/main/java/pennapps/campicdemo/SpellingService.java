package pennapps.campicdemo;

import android.service.textservice.SpellCheckerService;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.TextInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowen on 9/5/15.
 */
public class SpellingService extends SpellCheckerService {
    @Override
    public Session createSession() {
        return new MySpellingSession();
    }

    class MySpellingSession extends Session {
        @Override
        public void onCreate() {

        }

        @Override
        public SuggestionsInfo onGetSuggestions(TextInfo textInfo, int suggestionsLimit) {
            String word = textInfo.getText();
            int attr = SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO;
            String suggestions[] = null;
            SuggestionsInfo suggestionsInfo = new SuggestionsInfo(attr, suggestions);
            return suggestionsInfo;
        }

        @Override
        public SentenceSuggestionsInfo[] onGetSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) {

            List<SuggestionsInfo> suggestionsInfos = new ArrayList<>();

            for(int i=0; i<textInfos.length; i++){
                TextInfo cur = textInfos[i];

                // Convert the sentence into an array of words
                String words[] = cur.getText().split("\\s+");
                for(String word:words){
                    TextInfo tmp = new TextInfo(word);
                    // Generate suggestions for each word
                    suggestionsInfos.add(onGetSuggestions(tmp, suggestionsLimit));
                }
            }
            return new SentenceSuggestionsInfo[]{
                    new SentenceSuggestionsInfo(
                            suggestionsInfos.toArray(new SuggestionsInfo[suggestionsInfos.size()]),
                            new int[suggestionsInfos.size()],
                            new int[suggestionsInfos.size()]
                    )
            };
        }
    }
}
