import type {BookDTO} from "@/types/book-dto.ts";
import {Link} from "react-router";


const BookCard = (props: BookDTO) => {
    return (
        <Link className={"flex flex-col gap-3 min-w-44 md:min-w-auto w-full md:max-w-70"} to={`/book/${props.id}`} onClick={() => console.log(props.id)}>
            <div className="overflow-hidden rounded-md">
                <img
                    src={props.coverUrl}
                    alt={props.title}
                    className={"h-auto w-full object-cover transition-all hover:scale-105 aspect-5/8"}
                />
            </div>

            <div className="flex flex-col min-h-[3.5rem] gap-1 text-sm">
                <h3 className="font-medium leading-none line-clamp-2">{props.title}</h3>
                <p className="text-xs text-muted-foreground line-clamp-1">{props.author}</p>
            </div>
        </Link>
    );
};

export default BookCard;