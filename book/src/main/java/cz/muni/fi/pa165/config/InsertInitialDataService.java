package cz.muni.fi.pa165.config;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.data.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class InsertInitialDataService {
    private final BookRepository bookRepository;

    @Autowired
    public InsertInitialDataService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void insertInitialData() {
        insertKieraCassBooks();
        insertDanBrownBooks();
        insertAgathaChristieBooks();
    }

    private void insertKieraCassBooks() {
        String author = "Kiera Cassová";

        String title1 = "Selekcia";
        String description1 = "Pre tridsaťpäť dievčat je Selekcia životnou šancou. Príležitosť uniknúť svojmu osudu, " +
                "vstúpiť do okázalého sveta rób a drahých šperkov...";
        Book book1 = new Book(title1, author, description1, BookStatus.AVAILABLE);

        String title2 = "Elita";
        String description2 = "Z 35 dievčat v Selekcii zostalo už len 6 elitných súperiek. America spolu s nimi " +
                "naďalej bojuje o lásku princa Maxona, avšak stále si nie je istá, komu skutočne patrí jej srdce...";
        Book book2 = new Book(title2, author, description2, BookStatus.BORROWED);

        String title3 = "Prvá";
        String description3 = "Selekcia sa blíži k záveru a v súťaži zostali len 4 dievčatá. America je pevne " +
                "rozhodnutá získať Maxona pre seba, a hoci sa opätovne zblížili, Maxon stále netuší, kto je Aspen...";
        Book book3 = new Book(title3, author, description3, BookStatus.RESERVED);

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    private void insertDanBrownBooks() {
        String author = "Dan Brown";

        String title1 = "Bod klamu";
        String description1 = "V neprístupných končinách Arktídy objaví satelit NASA pod hrubou vrstvou ľadu " +
                "unikátny nález – tristo rokov starý meteorit so stopami fosílií...";
        Book book1 = new Book(title1, author, description1, BookStatus.AVAILABLE);

        String title2 = "Stratený symbol";
        String description2 = "Známeho harvardského odborníka na ikonografiu Roberta Langdona nečakane pozvú " +
                "prednášať do washingtonského Kapitolu. Pozvanie mu cez sprostredkovateľa zašle jeho mentor, " +
                "filantrop, historik, vedec a slobodomurár Peter Solomon, preto Langdon rád vyhovie...";
        Book book2 = new Book(title2, author, description2, BookStatus.AVAILABLE);

        String title3 = "Da Vinciho kód";
        String description3 = "Robert Langdon, harvardský vedec zaoberajúci sa symbolmi, je pracovne v Paríži a " +
                "neskoro v noci dostane naliehavý telefonát. V Louvri bol zavraždený kurátor múzea a pri jeho " +
                "tele sa našla záhadná šifra...";
        Book book3 = new Book(title3, author, description3, BookStatus.RESERVED);

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    private void insertAgathaChristieBooks() {
        String author = "Agatha Christie";

        String title1 = "Vražda v Orient exprese";
        String description1 = "Vražda v Orient exprese z roku 1934 je jednou z najlepších a najslávnejších detektívok " +
                "legendárnej Agathy Christie. Tento strhujúci príbeh celé roky fascinuje nielen čitateľov, ale aj " +
                "filmových tvorcov...";
        Book book1 = new Book(title1, author, description1, BookStatus.BORROWED);

        String title2 = "Mŕtva v lodenici";
        String description2 = "Manželia Stubbsovci, bohatí majitelia sídla Nasse House v Devone, dostanú nezvyčajný " +
                "nápad usporiadať pre hostí letnej slávnosti hru na vraždu a jej organizovaním poveria známu " +
                "autorku detektívok Ariadne Oliverovú...";
        Book book2 = new Book(title2, author, description2, BookStatus.AVAILABLE);

        String title3 = "Jed v Šampanskom";
        String description3 = "Narodeninová oslava krásnej dedičky sa náhle zmení na nočnú moru, keď Rosemary " +
                "Bartonová vypije otrávené šampanské a s tvárou skrivenou od bolesti sa zrúti mŕtva na stôl...";
        Book book3 = new Book(title3, author, description3, BookStatus.BORROWED);

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }
}
