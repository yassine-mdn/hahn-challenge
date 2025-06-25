import {Button} from "@/components/ui/button.tsx";
import {Rating, RatingButton} from "@/components/ui/rating.tsx";
import {Separator} from "@/components/ui/separator.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import BookCard from "@/features/books/components/book-card.tsx";
import {ScrollArea, ScrollBar} from "@/components/ui/scroll-area.tsx";
import RatingDistribution from "@/features/books/components/rating-distribution.tsx";
import UserReviews from "@/features/books/components/user-reviews.tsx";
import {ReadingStatusModal} from "@/features/books/components/reading-status-modal.tsx";
import {useQuery} from "@tanstack/react-query";
import {useParams, useNavigate} from "react-router";
import {fetchBookDetails, fetchRatings, fetchSimilarBooks} from "@/services/book.service.ts";
import {fetchReadingListItemByBookId} from "@/services/reading-list.service.ts";
import {useAuth} from "@/features/auth/AuthContext.tsx";
import {getBookGenreLabel} from "@/types/book-dto.ts";
import {fetchBookReviews} from "@/services/review.service.ts";
import {getReadingListStatusLabel} from "@/types/reading-list-dto.ts";
import {useState} from "react";

const BookDetails = () => {
    const {id} = useParams<{ id: string }>();
    const {user, isAuthenticated} = useAuth();
    const navigate = useNavigate();
    const [modalOpen, setModalOpen] = useState(false);
    const bookId = parseInt(id || "1");

    const book = useQuery({
        queryKey: ["book", bookId],
        queryFn: () => fetchBookDetails(bookId),
        enabled: !!bookId,
    });

    const similarBooks = useQuery({
        queryKey: ["similarBooks", bookId],
        queryFn: () => fetchSimilarBooks(bookId),
        enabled: !!bookId,
    });

    const reviews = useQuery({
        queryKey: ["reviews", bookId],
        queryFn: () => fetchBookReviews(bookId),
        enabled: !!bookId,
    });


    const ratings = useQuery({
        queryKey: ["ratings", bookId],
        queryFn: () => fetchRatings(bookId),
        enabled: !!bookId,
    });


    const readingList = useQuery({
        queryKey: ["userReadingItem", user, bookId],
        queryFn: () => fetchReadingListItemByBookId(user!, bookId),
        enabled: isAuthenticated && !!bookId,
    });

    const handleReadingListClick = () => {
        if (!isAuthenticated) {
            navigate("/login");
            return;
        }
        setModalOpen(true);
    };

    const getButtonText = () => {
        if (!isAuthenticated) {
            return "Add to list";
        }
        
        if (readingList.isLoading) {
            return "Loading...";
        }
        
        if (readingList.isSuccess && readingList.data) {
            return getReadingListStatusLabel(readingList.data.status!);
        }
        
        return "Add to list";
    };

    const getButtonVariant = () => {
        if (!isAuthenticated) {
            return "outline" as const;
        }
        
        if (readingList.isSuccess && readingList.data) {
            return "default" as const;
        }
        
        return "outline" as const;
    };

    const getModalMode = () => {
        if (readingList.isSuccess && readingList.data) {
            return "update" as const;
        }
        return "create" as const;
    };

    if (!book) {
        return <div className="p-4">Book not found</div>;
    }

    if (!book.isSuccess || !similarBooks.isSuccess || !reviews.isSuccess || !ratings.isSuccess) {
        return <div className="p-4">Loading...</div>;
    }

    return (
        <div className={"md:space-x-4"}>
            <div className="flex flex-wrap lg:flex-nowrap justify-center gap-4 h-screen p-4">
                <div className="flex flex-col w-full lg:w-80 gap-4 lg:flex-shrink-0">
                    <div className="rounded-lg overflow-hidden">
                        <img src={book.data.coverUrl} className={"h-auto w-full object-cover aspect-5/8"}/>
                    </div>
                    <Button 
                        variant={getButtonVariant()} 
                        className={"rounded-full"}
                        onClick={handleReadingListClick}
                        disabled={readingList.isLoading}
                    >
                        {getButtonText()}
                    </Button>
                    <Rating 
                        className={"self-center"} 
                        value={readingList.data?.rating || 0}
                        readOnly
                    >
                        {Array.from({length: 5}).map((_, index) => (
                            <RatingButton key={index}/>
                        ))}
                    </Rating>
                </div>
                <div className="flex flex-col gap-4 flex-1 min-h-0 overflow-y-auto no-scrollbar">
                    <div className="flex-shrink-0">
                        <h3 className="text-3xl sm:text-4xl lg:text-6xl font-medium">{book.data.title}</h3>
                        <h3 className="text-xl sm:text-2xl mt-1 text-muted-foreground hover:underline">{book.data.author}</h3>
                    </div>
                    <Separator className="flex-shrink-0"/>
                    <p className="text-wrap">{book.data.description}</p>
                    <div className="flex flex-wrap items-center gap-2">
                        {book.data.genres?.map((genre) => (
                            <Badge
                                key={genre}
                                variant="secondary"
                                className="rounded-full p-0.5 gap-2 px-3 py-1.5 font-normal"
                            >
                                {getBookGenreLabel(genre)}
                            </Badge>
                        ))}
                    </div>
                    <Separator/>
                    <div className="space-y-1 ">
                        <h2 className="text-xl font-semibold tracking-tight">
                            Readers also enjoyed
                        </h2>
                    </div>
                    <ScrollArea>
                        <div className="flex space-x-4 pb-4">
                            {similarBooks.data && similarBooks.data.content.map((book) => (
                                        <div key={book.id} className={"w-52"}>
                                            <BookCard
                                                id={book.id}
                                                title={book.title}
                                                author={book.author}
                                                publisher={book.publisher}
                                                genres={book.genres}
                                                description={book.description}
                                                coverUrl={book.coverUrl}
                                            />
                                        </div>
                                    )
                                )}
                        </div>
                        <ScrollBar orientation="horizontal"/>
                    </ScrollArea>
                    <Separator/>
                    <h2 className="text-xl font-semibold tracking-tight">
                        Community reviews
                    </h2>
                    <div className={"w-full"}>
                         <RatingDistribution ratings={ratings.data}/>
                    </div>
                    <div className="mx-auto space-y-4">
                        {reviews.data.content.map((review) => (
                                <UserReviews key={review.id} review={review}/>
                        ))}
                    </div>
                </div>
            </div>

            {isAuthenticated && user && (
                <ReadingStatusModal
                    open={modalOpen}
                    onOpenChange={setModalOpen}
                    bookId={bookId}
                    bookTitle={book.data.title || "Unknown Book"}
                    username={user!}
                    mode={getModalMode()}
                    currentReadingItem={readingList.data || null}
                />
            )}
        </div>

    );
};

export default BookDetails;