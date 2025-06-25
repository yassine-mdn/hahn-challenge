import BookCard from "@/features/books/components/book-card.tsx";
import {useQuery} from "@tanstack/react-query";
import {fetchFeaturedBooks, fetchPopularBooks} from "@/services/book.service.ts";


const Home = () => {

    const popular = useQuery({
        queryKey: ["popular-books"],
        queryFn: () => fetchPopularBooks(),
    })

    const featured = useQuery({
        queryKey: ["featured-books"],
        queryFn: () => fetchFeaturedBooks(),
    })

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
                {popular.isSuccess && popular.data.content.map((book) => (
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
                {featured.isSuccess && featured.data.content.map((book) => (
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