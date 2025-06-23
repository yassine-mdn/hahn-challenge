import BookCard from "@/components/book-card.tsx";
import {type BookDTO, BookDTOGenresEnum} from "@/types/book-dto";


const sampleBooks: BookDTO[] = [
    {
        id: 1,
        title: "1984",
        author: "George Orwell",
        publisher: "Secker & Warburg",
        genres: [BookDTOGenresEnum.DYSTOPIAN, BookDTOGenresEnum.SCIENCEFICTION],
        description: "A dystopian social science fiction novel and cautionary tale.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: true,
    },
    {
        id: 2,
        title: "To Kill a Mockingbird",
        author: "Harper Lee",
        publisher: "J.B. Lippincott & Co.",
        genres: [BookDTOGenresEnum.CLASSIC, BookDTOGenresEnum.HISTORICALFICTION],
        description: "A novel about the serious issues of rape and racial inequality.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: false,
    },
    {
        id: 3,
        title: "A Brief History of Time",
        author: "Stephen Hawking",
        publisher: "Bantam Dell Publishing Group",
        genres: [BookDTOGenresEnum.SCIENCE, BookDTOGenresEnum.NONFICTION],
        description: "An overview of cosmology for the general public.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: true,
    },
    {
        id: 4,
        title: "The Hobbit",
        author: "J.R.R. Tolkien",
        publisher: "George Allen & Unwin",
        genres: [BookDTOGenresEnum.FANTASY, BookDTOGenresEnum.CLASSIC],
        description: "A fantasy novel and children's book by J.R.R. Tolkien.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: false,
    },
    {
        id: 5,
        title: "The Art of War",
        author: "Sun Tzu",
        publisher: "Oxford University Press",
        genres: [BookDTOGenresEnum.PHILOSOPHY, BookDTOGenresEnum.HISTORY],
        description: "An ancient Chinese military treatise dating from the Late Spring and Autumn Period.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: false,
    },
    {
        id: 6,
        title: "The Art of War",
        author: "Sun Tzu",
        publisher: "Oxford University Press",
        genres: [BookDTOGenresEnum.PHILOSOPHY, BookDTOGenresEnum.HISTORY],
        description: "An ancient Chinese military treatise dating from the Late Spring and Autumn Period.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: false,
    },
    {
        id: 1,
        title: "1984",
        author: "George Orwell",
        publisher: "Secker & Warburg",
        genres: [BookDTOGenresEnum.DYSTOPIAN, BookDTOGenresEnum.SCIENCEFICTION],
        description: "A dystopian social science fiction novel and cautionary tale.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: true,
    },
    {
        id: 2,
        title: "To Kill a Mockingbird",
        author: "Harper Lee",
        publisher: "J.B. Lippincott & Co.",
        genres: [BookDTOGenresEnum.CLASSIC, BookDTOGenresEnum.HISTORICALFICTION],
        description: "A novel about the serious issues of rape and racial inequality.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: false,
    },
    {
        id: 3,
        title: "A Brief History of Time",
        author: "Stephen Hawking",
        publisher: "Bantam Dell Publishing Group",
        genres: [BookDTOGenresEnum.SCIENCE, BookDTOGenresEnum.NONFICTION],
        description: "An overview of cosmology for the general public.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: true,
    },
    {
        id: 4,
        title: "The Hobbit",
        author: "J.R.R. Tolkien",
        publisher: "George Allen & Unwin",
        genres: [BookDTOGenresEnum.FANTASY, BookDTOGenresEnum.CLASSIC],
        description: "A fantasy novel and children's book by J.R.R. Tolkien.",
        coverUrl: "https://cdn.hmv.com/r/w-1280/hmv/files/13/13e34619-840e-4bbc-9558-0d076baf3b9d.jpg",
        isFeatured: false,
    },

];


const Home = () => {
    return (
        <section id={"home"} className={"overflow-x-clip"}>
            <div className="flex items-center justify-between my-6">
                <div className="space-y-1">
                    <h2 className="text-2xl font-semibold tracking-tight">
                        Popular Books
                    </h2>
                    <p className="text-sm text-muted-foreground">
                        Top picks for you. Updated daily.
                    </p>
                </div>
            </div>
            <div className="grid place-items-center grid-cols-2 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-4 pb-4 mx-auto max-w-full overflow-x-hidden">
                {sampleBooks.map((book) => (
                    <BookCard
                        id={book.id}
                        title={book.title}
                        author={book.author}
                        publisher={book.publisher}
                        genres={book.genres}
                        description={book.description}
                        coverUrl={book.coverUrl}
                    />
                ))}
            </div>
            <div className="mt-6 space-y-1 my-6">
                <h2 className="text-2xl font-semibold tracking-tight">
                    Featured Books
                </h2>
                <p className="text-sm text-muted-foreground">
                    A collection of books handpicked by our contributors.
                </p>
            </div>
            <div className="grid place-items-center grid-cols-2 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-4 pb-4 mx-auto overflow-x-hidden">
                {sampleBooks.map((book) => (
                    <BookCard
                        id={book.id}
                        title={book.title}
                        author={book.author}
                        publisher={book.publisher}
                        genres={book.genres}
                        description={book.description}
                        coverUrl={book.coverUrl}
                    />
                ))}
            </div>
        </section>
    );
};

export default Home;