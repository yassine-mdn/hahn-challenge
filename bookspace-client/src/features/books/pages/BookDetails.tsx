import {type BookDTO, BookDTOGenresEnum} from "@/types/book-dto.ts";
import {Button} from "@/components/ui/button.tsx";
import {Rating, RatingButton} from "@/components/ui/rating.tsx";
import {Separator} from "@/components/ui/separator.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import BookCard from "@/features/books/components/book-card.tsx";
import {ScrollArea, ScrollBar} from "@/components/ui/scroll-area.tsx";
import type {RatingCountDTO} from "@/types/rating-count-dto.ts";
import RatingDistribution from "@/features/books/components/rating-distribution.tsx";
import type {ReviewDTO} from "@/types/review-dto.ts";
import UserReviews from "@/features/books/components/user-reviews.tsx";

const bookDetails: BookDTO = {
    id: 1,
    title: "1984",
    author: "George Orwell",
    publisher: "Secker & Warburg",
    genres: [BookDTOGenresEnum.DYSTOPIAN, BookDTOGenresEnum.SCIENCEFICTION],
    description: "A masterpiece of rebellion and imprisonment where war is peace freedom is slavery and Big Brother is watching. Thought Police, Big Brother, Orwellian - these words have entered our vocabulary because of George Orwell's classic dystopian novel 1984. The story of one man's Nightmare Odyssey as he pursues a forbidden love affair through a world ruled by warring states and a power structure that controls not only information but also individual thought and memory 1984 is a prophetic haunting tale More relevant than ever before 1984 exposes the worst crimes imaginable the destruction of truth freedom and individuality. With a foreword by Thomas Pynchon. This beautiful paperback edition features deckled edges and french flaps a perfect gift for any occasion",
    coverUrl: "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1657781256i/61439040.jpg",
    isFeatured: true,
}

const similarBooks: BookDTO[] = [
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

const ratingsData: RatingCountDTO[] = [
    {rating: 5, count: 2450580},
    {rating: 4, count: 1703437},
    {rating: 3, count: 729960},
    {rating: 2, count: 193492},
    {rating: 1, count: 103102},
];

const sampleReviews: ReviewDTO[] = [
    {
        id: 1,
        username: "Sarah Johnson",
        comment:
            "Absolutely love this product! The quality is outstanding and it exceeded my expectations. The customer service was also fantastic. Highly recommend to anyone looking for a reliable solution.",
        rating: 5,
        createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000), // 2 days ago
    },
    {
        id: 2,
        username: "Mike Chen",
        comment:
            "Good value for money. Works as expected, though the setup could be a bit more intuitive. Overall satisfied with the purchase.",
        rating: 4,
        createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000), // 1 week ago
    },
    {
        id: 3,
        username: "Emily Rodriguez",
        comment: "Perfect! Exactly what I was looking for. Fast shipping and great packaging too.",
        rating: 5,
        createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000), // 1 day ago
    },
]


const BookDetails = () => {

    //fetch Book by Id
    //fetch Reviews
    //fetch Ratings
    //if user connected fetch Reading status and review if exist
    return (
        <div className={"md:space-x-4"}>
            <div className="flex flex-wrap lg:flex-nowrap justify-center gap-4 h-screen p-4">
                <div className="flex flex-col w-full lg:w-80 gap-4 lg:flex-shrink-0">
                    <div className="rounded-lg overflow-hidden">
                        <img src={bookDetails.coverUrl} className={"h-auto w-full object-cover aspect-5/8"}/>
                    </div>
                    <Button variant={"outline"} className={"rounded-full"}>Add to list</Button>
                    <Rating className={"self-center"} defaultValue={3}>
                        {Array.from({length: 5}).map((_, index) => (
                            <RatingButton key={index}/>
                        ))}
                    </Rating>
                </div>
                <div className="flex flex-col gap-4 flex-1 min-h-0 overflow-y-auto no-scrollbar">
                    <div className="flex-shrink-0">
                        <h3 className="text-3xl sm:text-4xl lg:text-6xl font-medium">{bookDetails.title}</h3>
                        <h3 className="text-xl sm:text-2xl mt-1 text-muted-foreground hover:underline">{bookDetails.author}</h3>
                    </div>
                    <Separator className="flex-shrink-0"/>
                    <p className="text-wrap">{bookDetails.description}</p>
                    <div className="flex flex-wrap items-center gap-2">
                        {bookDetails.genres?.map((genre) => (
                            <Badge
                                key={genre}
                                variant="secondary"
                                className="rounded-full p-0.5 gap-2 px-3 py-1.5 font-normal"
                            >
                                {genre}
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
                            {similarBooks.map((book) => (
                                <div className={"w-52"}>
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
                            ))}
                        </div>
                        <ScrollBar orientation="horizontal"/>
                    </ScrollArea>
                    <Separator/>
                    <h2 className="text-xl font-semibold tracking-tight">
                        Community reviews
                    </h2>
                    <div className={"w-full"}>
                        <RatingDistribution ratings={ratingsData}/>
                    </div>
                    <div className="mx-auto space-y-4">
                        {sampleReviews.map((review) => (
                            <UserReviews key={review.id} review={review}/>
                        ))}
                    </div>
                </div>
            </div>

        </div>
    );
};

export default BookDetails;