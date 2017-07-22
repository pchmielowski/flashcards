package net.chmielowski.fiszki;

import android.support.annotation.NonNull;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class DictionaryUtils {
    private static List<String> groups() {
        String WORDS =
                "στέλνω ,I send,στέλνεις ,you send,στέλνει ,he/she sends,στέλνουμε ,we send,στέλνετε ,you all send,στέλνουν ,they send\n"
                        + "φέρνω ,I bear,φέρνεις ,you bear,φέρνει ,he/she bears,φέρνουμε ,we bear,φέρνετε ,you all bear,φέρνουν ,they bear\n"
                        + "περιμένω ,I wait,περιμένεις ,you wait,περιμένει ,he/she waits,περιμένουμε ,we wait,περιμένετε ,you all wait,περιμένουν ,they wait\n"
                        + "χρησιμοποιώ ,I use,χρησιμοποιείς ,you use,χρησιμοποιεί ,he/she uses,χρησιμοποιούμε ,we use,χρησιμοποιείτε ,you all use,χρησιμοποιούν ,they use\n"
                        + "σκέφτομαι ,I think,σκέφτεσαι ,you think,σκέφτεται ,he/she thinks,σκεφτόμαστε ,we think,σκέφτεστε ,you all think,σκέφτονται ,they think\n"
                        + "χρειάζομαι ,I need,χρειάζεσαι ,you need,χρειάζεται ,he/she does need,χρειαζόμαστε ,we need,χρειάζεστε ,you all all need,χρειάζονται ,they need\n"
                        + "μαγειρεύω ,I cook,μαγειρεύεις ,you cook,μαγειρεύει ,he/she cooks,μαγειρεύουμε ,we cook,μαγειρεύετε ,you all cook,μαγειρεύουν ,they cook\n"
                        + "απαντώ ,I reply,απαντάς ,you reply,απαντά ,he/she replies,απαντούμε ,we reply,απαντάτε ,you all reply,απαντούν ,they reply\n"
                        + "βάζω ,I put,βάζεις ,you put,βάζει ,he/she puts,βάζουμε ,we put,βάζετε ,you all put,βάζουν ,they put\n"
                        + "βγαίνω ,I go out,βγαίνεις ,you go out,βγαίνει ,he/she goes out,βγαίνουμε ,we go out,βγαίνετε ,you all go out,βγαίνουν ,they go out\n"
                        + "περνώ ,I do,περνάς ,you do,περνά ,he/she does,περνούμε ,we do,περνάτε ,you all do,περνούν ,they do\n"
                        + "γίνομαι ,I become,γίνεσαι ,you become,γίνεται ,he/she does become,γινόμαστε ,we become,γίνεστε ,you all all become,γίνονται ,they become\n"
                        + "πάω ,I go,πας ,you go,πάει ,he/she goes,πάμε ,we go,πάτε ,you all go,πάνε ,they go\n"
                        + "αγοράζω ,I buy,αγοράζεις ,you buy,αγοράζει ,he/she buys,αγοράζουμε ,we buy,αγοράζετε ,you all buy,αγοράζουν ,they buy\n"
                        + "κάθομαι ,I sit,κάθεσαι ,you sit,κάθεται ,he/she does sit,καθόμαστε ,we sit,κάθεστε ,you all all sit,κάθονται ,they sit\n"
                        + "καταλαβαίνω ,I understand,καταλαβαίνεις ,you understand,καταλαβαίνει ,he/she understands,καταλαβαίνουμε ,we understand,καταλαβαίνετε ,you all understand,καταλαβαίνουν ,they understand\n"
                        + "θυμάμαι ,I remember,θυμάσαι ,you remember,θυμάται ,he/she remembers,θυμόμαστε ,we remember,θυμάστε ,you all remember,θυμούνται ,they remember\n"
                        + "ξέρω ,I know,ξέρεις ,you know,ξέρει ,he/she does know,ξέρουμε ,we know,ξέρετε ,you all all know,ξέρουν ,they know\n"
                        + "φτάνω ,I arrive,φτάνεις ,you arrive,φτάνει ,he/she arrives,φτάνουμε ,we arrive,φτάνετε ,you all arrive,φτάνουν ,they arrive\n"
                        + "μαθαίνω ,I learn,μαθαίνεις ,you learn,μαθαίνει ,he/she learns,μαθαίνουμε ,we learn,μαθαίνετε ,you all learn,μαθαίνουν ,they learn\n"
                        + "γράφω ,I write,γράφεις ,you write,γράφει ,he/she writes,γράφουμε ,we write,γράφετε ,you all write,γράφουν ,they write\n"
                        + "δουλεύω ,I work,δουλεύεις ,you work,δουλεύει ,he/she works,δουλεύουμε ,we work,δουλεύετε ,you all work,δουλεύουν ,they work\n"
                        + "φεύγω ,I leave,φεύγεις ,you leave,φεύγει ,he/she leaves,φεύγουμε ,we leave,φεύγετε ,you all leave,φεύγουν ,they leave\n"
                        + "έχω ,I have,έχεις ,you have,έχει ,he/she has,έχουμε ,we have,έχετε ,you all have,έχουν ,they have\n"
                        + "κοιμάμαι ,I sleep,κοιμάσαι ,you sleep,κοιμάται ,he/she sleeps,κοιμόμαστε ,we sleep,κοιμάστε ,you all sleep,κοιμούνται ,they sleep\n"
                        + "μένω ,I stay,μένεις ,you stay,μένει ,he/she stays,μένουμε ,we stay,μένετε ,you all stay,μένουν ,they stay\n"
                        + "παίζω ,I play,παίζεις ,you play,παίζει ,he/she plays,παίζουμε ,we play,παίζετε ,you all play,παίζουν ,they play\n"
                        + "δίνω ,I give,δίνεις ,you give,δίνει ,he/she gives,δίνουμε ,we give,δίνετε ,you all give,δίνουν ,they give\n"
                        + "ακούω  ,I hear,ακούς ,you hear,ακούει ,he/she hears,ακούμε ,we hear,ακούτε ,you all hear,ακούν ,they hear\n"
                        + "έρχομαι ,I come,έρχεσαι ,you come,έρχεται ,he/she does come,ερχόμαστε ,we come,έρχεστε ,you all all come,έρχονται ,they come\n"
                        + "πάω ,I go,πας ,you go,πάει ,he/she goes,πάμε ,we go,πάτε ,you all go,πάνε ,they go\n"
                        + "διαβάζω ,I read,διαβάζεις ,you read,διαβάζει ,he/she reads,διαβάζουμε ,we read,διαβάζετε ,you all read,διαβάζουν ,they read\n"
                        + "βρίσκω ,I find,βρίσκεις ,you find,βρίσκει ,he/she finds,βρίσκουμε ,we find,βρίσκετε ,you all find,βρίσκουν ,they find\n"
                        + "έχω ,I have,έχεις ,you have,έχει ,he/she has,έχουμε ,we have,έχετε ,you all have,έχουν ,they have\n"
                        + "παίρνω ,I take,παίρνεις ,you take,παίρνει ,he/she takes,παίρνουμε ,we take,παίρνετε ,you all take,παίρνουν ,they take\n"
                        + "πίνω ,I drink,πίνεις ,you drink,πίνει ,he/she drinks,πίνουμε ,we drink,πίνετε ,you all drink,πίνουν ,they drink\n"
                        + "έρχομαι ,I come,έρχεσαι ,you come,έρχεται ,he/she does come,ερχόμαστε ,we come,έρχεστε ,you all all come,έρχονται ,they come\n"
                        + "μπορώ ,I can,μπορείς ,you can,μπορεί ,he/she does can,μπορούμε ,we can,μπορείτε ,you all can,μπορούν ,they can\n"
                        + "τρώω ,I eat,τρως ,you eat,τρώει ,he/she eats,τρώμε ,we eat,τρώτε ,you all eat,τρώνε ,they eat\n"
                        + "λέω ,I say,λες ,you say,λέει ,he/she says,λέμε ,we say,λέτε ,you all say,λένε ,they say\n"
                        + "θέλω ,I want,θέλεις ,you want,θέλει ,he/she does want,θέλουμε ,we want,θέλετε ,you all all want,θέλουν ,they want\n"
                        + "μιλάω ,I speak,μιλάς ,you speak,μιλά ,he/she speaks,μιλούμε ,we speak,μιλάτε ,you all speak,μιλούν ,they speak\n"
                        + "κάνω ,I do,κάνεις ,you do,κάνει ,he/she does,κάνουμε ,we do,κάνετε ,you all do,κάνουν ,they do\n"
                        + "πηγαίνο,I go,πας ,you go,πάει ,he/she goes,πάμε ,we go,πάτε ,you all go,πάνε ,they go\n"
                        + "βλέπω ,I see,βλέπεις ,you see,βλέπει ,he/she sees,βλέπουμε ,we see,βλέπετε ,you all see,βλέπουν ,they see\n"
                        + "είμαι ,I am,είσαι ,you are,είναι ,he/she is,είμαστε ,we are,είστε ,you all are,είναι ,they are";
        return Arrays.asList(WORDS.split("\n"));
    }

    private static String extract(String word) {return word.replace("\"", "");}

    static List<Word> shuffled() {
        final List<String> groups = groups();
        Collections.shuffle(groups);
        final List<Word> words = Stream.of(groups)
                                       .limit(3)
                                       .flatMap(splitter())
                                       .toList();
        Collections.shuffle(words);
        return Stream.of(words).limit(20).toList();
    }

    @NonNull
    private static Function<String, Stream<? extends Word>> splitter() {
        return row -> {
            final String[] words = row.split(",");
            return IntStream.iterate(0, i -> i + 2).limit(words.length / 2)
                            .boxed()
                            .map(idx -> new Word(
                                    extract(words[idx + 1]), extract(words[idx])));
        };
    }
}
