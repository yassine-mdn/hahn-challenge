import type {ColumnDef} from "@tanstack/react-table";
import type {BookDTO} from "@/types/book-dto.ts";
import {getBookGenreLabel} from "@/types/book-dto.ts";
import {Badge} from "@/components/ui/badge.tsx";
import {Checkbox} from "@/components/ui/checkbox.tsx";
import { BookActions } from "./book-actions";

export const columns: ColumnDef<BookDTO>[] = [
  {
    accessorKey: "coverUrl",
    header: "Cover",
    cell: ({ row }) => {
      const url = row.original.coverUrl;
      return url ? (
        <img src={url} alt={row.original.title} className="object-cover w-16 aspect-5/8 rounded-lg" />
      ) : (
        <span style={{ color: '#aaa' }}>No Image</span>
      );
    },
    size: 60,
    enableSorting: false,
  },
  {
    accessorKey: "title",
    header: "Title",
  },
  {
    accessorKey: "author",
    header: "Author",
  },
  {
    accessorKey: "publisher",
    header: "Publisher",
  },
  {
    accessorKey: "genres",
    header: "Genres",
    cell: ({ row }) => {
      const genres = row.original.genres;
      if (!genres || genres.length === 0) return null;
      const maxToShow = 4;
      const shown = genres.slice(0, maxToShow);
      const rest = genres.length - maxToShow;
      return (
        <>
          {shown.map((g, i) => (
            <Badge
              key={g + i}
              variant="secondary"
              className="rounded-full p-0.5 gap-2 px-3 py-1.5 font-normal mr-2"
            >
              {getBookGenreLabel(g)}
            </Badge>
          ))}
          {rest > 0 && (
            <Badge
              variant="secondary"
              className="rounded-full p-0.5 gap-2 px-3 py-1.5 font-normal mr-2"
            >
              +{rest}
            </Badge>
          )}
        </>
      );
    },
  },
  {
    accessorKey: "isFeatured",
    header: "Featured",
    cell: ({ row }) => (
      <Checkbox 
        checked={row.original.isFeatured}
        className="pointer-events-none"
      />
    ),
    size: 80,
  },
  {
    id: "actions",
    cell: ({ row }) => {
      const book = row.original

      return (
        <BookActions book={book} />
      )
    },
  },
];