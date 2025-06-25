import {type ReadingListDTO} from "@/types/reading-list-dto";
import BookCard from "@/features/books/components/book-card.tsx";
import {ScrollArea, ScrollBar} from "@/components/ui/scroll-area.tsx";
import {Link} from "react-router";
import { Card, CardContent } from "@/components/ui/card";

interface ReadingListProps {
  readingList: ReadingListDTO[];
}

export const ReadingList = ({ readingList }: ReadingListProps) => {

    const hasReadingListItems = readingList && readingList.length > 0;

  return (
    <div className={"overflow-x-auto"}>
      <div className="flex flex-col  justify-start items-start mb-4">
        <Link to={"reading-list"} className="text-xl font-semibold hover:underline cursor-pointer">Reading List</Link>
        <span className={"text-ld text-muted-foreground"}>Last added</span>
      </div>
        {hasReadingListItems ? (
            <ScrollArea>
                <div className="flex space-x-4 pb-4">
                    {readingList.map((rl) => (
                        rl.book && (
                            <BookCard
                                key={rl.book.id}
                                id={rl.book.id}
                                title={rl.book.title}
                                coverUrl={rl.book.coverUrl}
                            />
                        )
                    ))}
                </div>
                <ScrollBar orientation="horizontal"/>
            </ScrollArea>
        ) : (
            <Card className={"w-full bg-background"}>
                <CardContent className="h-60 grid place-items-center">No books in reading list yet.</CardContent>
            </Card>
        )}

    </div>
  );
};