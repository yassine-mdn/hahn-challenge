import { type ReadingListDTO } from "@/types/reading-list-dto";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Separator } from "@/components/ui/separator";

interface ReadingListProps {
  readingList: ReadingListDTO[];
}

export const ReadingList = ({ readingList }: ReadingListProps) => {
  const [activeTab, setActiveTab] = useState<string>("All");
  const tabs = [
    "All",
    "Plan to Read",
    "Reading",
    "Completed",
    "On Hold",
    "Dropped"
  ];

  const statusMap: Record<string, string> = {
    PLAN_TO_READ: "Plan to Read",
    READING: "Reading",
    COMPLETED: "Completed",
    ON_HOLD: "On Hold",
    DROPPED: "Dropped"
  };

  const filteredList = activeTab === "All"
    ? readingList
    : readingList.filter(item => statusMap[item.status ?? ""] === activeTab);

  return (
    <div>
        <Separator className="my-4" />
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center space-y-4 sm:space-y-0 mb-4">
        <span className="text-xl font-semibold">My Reading List</span>
        <div className="flex space-x-1">
          {tabs.map((tab) => (
            <Button
              key={tab}
              variant={activeTab === tab ? "default" : "ghost"}
              size="sm"
              onClick={() => setActiveTab(tab)}
              className={activeTab === tab ? "bg-primary" : ""}
            >
              {tab}
            </Button>
          ))}
        </div>
      </div>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Cover</TableHead>
            <TableHead>Title</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Added</TableHead>
            <TableHead>Started</TableHead>
            <TableHead>Completed</TableHead>
            <TableHead>Rating</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {filteredList.map((item) => (
            <TableRow key={item.id}>
              <TableCell>

                  {item.book?.coverUrl && (
                    <img src={item.book.coverUrl} alt={item.book.title} className="object-cover w-16 aspect-5/8 rounded-lg" />
                  )}
              </TableCell>
              <TableCell className="font-medium">{item.book?.title}</TableCell>
              <TableCell>
                <Badge
                  variant="secondary"
                  className={
                    item.status === "COMPLETED" ? "bg-green-100 text-green-800" :
                    item.status === "READING" ? "bg-blue-100 text-blue-800" :
                    item.status === "PLAN_TO_READ" ? "bg-gray-100 text-gray-800" :
                    item.status === "ON_HOLD" ? "bg-yellow-100 text-yellow-800" :
                    item.status === "DROPPED" ? "bg-red-100 text-red-800" :
                    "bg-gray-100 text-gray-800"
                  }
                >
                  {statusMap[item.status ?? ""] ?? item.status}
                </Badge>
              </TableCell>
              <TableCell>{item.addedAt ? new Date(item.addedAt).toLocaleDateString() : "-"}</TableCell>
              <TableCell>{item.startedAt ? new Date(item.startedAt).toLocaleDateString() : "-"}</TableCell>
              <TableCell>{item.completedAt ? new Date(item.completedAt).toLocaleDateString() : "-"}</TableCell>
              <TableCell>{item.rating ?? "-"}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};