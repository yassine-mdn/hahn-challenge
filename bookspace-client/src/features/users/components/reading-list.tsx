import {type ReadingListDTO} from "@/types/reading-list-dto";
import BookCard from "@/features/books/components/book-card.tsx";
import {ScrollArea, ScrollBar} from "@/components/ui/scroll-area.tsx";

interface ReadingListProps {
  readingList: ReadingListDTO[];
}

export const ReadingList = ({ readingList }: ReadingListProps) => {


  return (
    <div>
      <div className="flex flex-col  justify-start items-start mb-4">
        <span className="text-xl font-semibold">Reading List</span>
        <span className={"text-ld text-muted-foreground"}>Last added</span>
      </div>
      <ScrollArea>
        <div className="flex space-x-4 pb-4">
          {readingList.map((rl) => (
              <BookCard
                  id={rl.book?.id}
                  title={rl.book?.title}
                  coverUrl={rl.book?.coverUrl}
              />
          ))}
        </div>
        <ScrollBar orientation="horizontal"/>
      </ScrollArea>

    </div>
  );
};