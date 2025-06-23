import type {BookDTO} from "@/types";
import {Link} from "react-router";


const BookCard = (props: BookDTO) => {
    return (
        <Link className={"flex flex-col gap-3 w-fit"} to={`/book/${props.id}`} onClick={() => console.log(props.id)}>
            <div className="overflow-hidden rounded-md">
                <img
                    src={props.coverUrl}
                    alt={props.title}
                    className={"h-auto object-cover transition-all hover:scale-105 portrait w-[250px]"}
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