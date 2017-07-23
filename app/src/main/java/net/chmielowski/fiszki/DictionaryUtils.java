package net.chmielowski.fiszki;

import android.support.annotation.NonNull;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class DictionaryUtils {
    private static List<String> groups(Lang lang) {
        final String ITALIAN =
                "vivo ,I live,vivi ,you live,vive ,he/she/it lives,viviamo ,we live,vivete ,you all live,vivono ,they live\n"
                        + "vendo ,I sell,vendi ,you sell,vende ,he/she/it sells,vendiamo ,we sell,vendete ,you all sell,vendono ,they sell\n"
                        + "vedo ,I see,vedi ,you see,vede ,he/she/it sees,vediamo ,we see,vedete ,you all see,vedono ,they see\n"
                        + "uso ,I use something,usi ,you use something,usa ,he/she/it uses something,usiamo ,we use something,usate ,you all use something,usano ,they use something\n"
                        + "trovo ,I find,trovi ,you find,trova ,he/she/it finds,troviamo ,we find,trovate ,you all find,trovano ,they find\n"
                        + "mi sveglio ,I do,ti svegli ,you do,si sveglia ,he/she/it does,ci svegliamo ,we do,vi svegliate ,you all do,si svegliano ,they do\n"
                        + "studio ,I study,studi ,you study,studia ,he/she/it studies,studiamo ,we study,studiate ,you all study,studiano ,they study\n"
                        + "spiego ,I explain,spieghi ,you explain,spiega ,he/she/it explains,spieghiamo ,we explain,spiegate ,you all explain,spiegano ,they explain\n"
                        + "spendo ,I spend,spendi ,you spend,spende ,he/she/it spends,spendiamo ,we spend,spendete ,you all spend,spendono ,they spend\n"
                        + "spedisco ,I send,spedisci ,you send,spedisce ,he/she/it sends,spediamo ,we send,spedite ,you all send,spediscono ,they send\n"
                        + "scrivo ,I write,scrivi ,you write,scrive ,he/she/it writes,scriviamo ,we write,scrivete ,you all write,scrivono ,they write\n"
                        + "so ,I know information,sai ,you know information,sa ,he/she/it knows information,sappiamo ,we know information,sapete ,you all know information,sanno ,they know information\n"
                        + "rompo ,I break,rompi ,you break,rompe ,he/she/it breaks,rompiamo ,we break,rompete ,you all break,rompono ,they break\n"
                        + "rispondo ,I answer,rispondi ,you answer,risponde ,he/she/it answers,rispondiamo ,we answer,rispondete ,you all answer,rispondono ,they answer\n"
                        + "pulisco ,I clean,pulisci ,you clean,pulisce ,he/she/it cleans,puliamo ,we clean,pulite ,you all clean,puliscono ,they clean\n"
                        + "mi preoccupo ,I do,ti preoccupi ,you do,si preoccupa ,he/she/it does,ci preoccupiamo ,we do,vi preoccupate ,you all do,si preoccupano ,they do\n"
                        + "prendo ,I take,prendi ,you take,prende ,he/she/it takes,prendiamo ,we take,prendete ,you all take,prendono ,they take\n"
                        + "posso ,I can,puoi ,you can,può ,he/she/it can,possiamo ,we can,potete ,you all can,possono ,they can\n"
                        + "porto ,I bring,porti ,you bring,porta ,he/she/it brings,portiamo ,we bring,portate ,you all bring,portano ,they bring\n"
                        + "— ,I rain,— ,you rain,piove ,he/she/it rains,— ,we rain,— ,you all rain,piovono ,they rain\n"
                        + "pettino ,I comb,pettini ,you comb,pettina ,he/she/it combs,pettiniamo ,we comb,pettinate ,you all comb,pettinano ,they comb\n"
                        + "permetto ,I allow,permetti ,you allow,permette ,he/she/it allows,permettiamo ,we allow,permettete ,you all allow,permettono ,they allow\n"
                        + "perdo ,I lose,perdi ,you lose,perde ,he/she/it loses,perdiamo ,we lose,perdete ,you all lose,perdono ,they lose\n"
                        + "penso ,I think,pensi ,you think,pensa ,he/she/it thinks,pensiamo ,we think,pensate ,you all think,pensano ,they think\n"
                        + "parto ,I depart,parti ,you depart,parte ,he/she/it departs,partiamo ,we depart,partite ,you all depart,partono ,they depart\n"
                        + "parlo ,I talk,parli ,you talk,parla ,he/she/it talks,parliamo ,we talk,parlate ,you all talk,parlano ,they talk\n"
                        + "nuoto ,I swim,nuoti ,you swim,nuota ,he/she/it swims,nuotiamo ,we swim,nuotate ,you all swim,nuotano ,they swim\n"
                        + "metto ,I put,metti ,you put,mette ,he/she/it puts,mettiamo ,we put,mettete ,you all put,mettono ,they put\n"
                        + "mangio ,I eat,mangi ,you eat,mangia ,he/she/it eats,mangiamo ,we eat,mangiate ,you all eat,mangiano ,they eat\n"
                        + "leggo ,I read,leggi ,you read,legge ,he/she/it reads,leggiamo ,we read,leggete ,you all read,leggono ,they read\n"
                        + "gioco ,I play,giochi ,you play,gioca ,he/she/it plays,giochiamo ,we play,giocate ,you all play,giocano ,they play\n"
                        + "fumo ,I smoke,fumi ,you smoke,fuma ,he/she/it smokes,fumiamo ,we smoke,fumate ,you all smoke,fumano ,they smoke\n"
                        + "finisco ,I finish,finisci ,you finish,finisce ,he/she/it finishes,finiamo ,we finish,finite ,you all finish,finiscono ,they finish\n"
                        + "faccio ,I do,fai ,you do,fa ,he/she/it does,facciamo ,we do,fate ,you all do,fanno ,they do\n"
                        + "dormo ,I sleep,dormi ,you sleep,dorme ,he/she/it sleeps,dormiamo ,we sleep,dormite ,you all sleep,dormono ,they sleep\n"
                        + "dico ,I say,dici ,you say,dice ,he/she/it says,diciamo ,we say,dite ,you all say,dicono ,they say\n"
                        + "dico ,I say,dici ,you say,dice ,he/she/it says,diciamo ,we say,dite ,you all say,dicono ,they say\n"
                        + "dimentico ,I forget,dimentichi ,you forget,dimentica ,he/she/it forgets,dimentichiamo ,we forget,dimenticate ,you all forget,dimenticano ,they forget\n"
                        + "do ,I give,dai ,you give,dà ,he/she/it gives,diamo ,we give,date ,you all give,danno ,they give\n"
                        + "capisco ,I understand,capisci ,you understand,capisce ,he/she/it understands,capiamo ,we understand,capite ,you all understand,capiscono ,they understand\n"
                        + "cambio ,I exchange,cambi ,you exchange,cambia ,he/she/it exchanges,cambiamo ,we exchange,cambiate ,you all exchange,cambiano ,they exchange\n"
                        + "cado ,I fall,cadi ,you fall,cade ,he/she/it falls,cadiamo ,we fall,cadete ,you all fall,cadono ,they fall\n"
                        + "bevo ,I drink,bevi ,you drink,beve ,he/she/it drinks,beviamo ,we drink,bevete ,you all drink,bevono ,they drink\n"
                        + "ballo ,I dance,balli ,you dance,balla ,he/she/it dances,balliamo ,we dance,ballate ,you all dance,ballano ,they dance\n"
                        + "ho ,I have,hai ,you have,ha ,he/she/it has,abbiamo ,we have,avete ,you all have,hanno ,they have\n"
                        + "aspetto ,I wait for,aspetti ,you wait for,aspetta ,he/she/it waits for,aspettiamo ,we wait for,aspettate ,you all wait for,aspettano ,they wait for\n"
                        + "ascolto ,I listen,ascolti ,you listen,ascolta ,he/she/it listens,ascoltiamo ,we listen,ascoltate ,you all listen,ascoltano ,they listen\n"
                        + "apro ,I open,apri ,you open,apre ,he/she/it opens,apriamo ,we open,aprite ,you all open,aprono ,they open\n"
                        + "vado ,I go,vai ,you go,va ,he/she/it goes,andiamo ,we go,andate ,you all go,vanno ,they go";
        final String GREEK =
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
        switch (lang) {
            case ITALIAN:
                return Arrays.asList(GREEK.split("\n"));
            case GREEK:
                return Arrays.asList(GREEK.split("\n"));
            default:
                throw new IllegalArgumentException();
        }
    }

    private static String extract(String word) {return word.replace("\"", "");}

    static List<Word> shuffled(Lang lang) {
        final List<String> groups = groups(lang);
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

    enum Lang {
        GREEK,
        ITALIAN
    }
}
