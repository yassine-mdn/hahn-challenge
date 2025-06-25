import { useForm } from "react-hook-form"
import type { BookDTO } from "@/types/book-dto"
import { BookDTOGenresEnum, getBookGenreLabel } from "@/types/book-dto"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Checkbox } from "@/components/ui/checkbox"
import { MultiSelect } from "@/components/ui/muti-select"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"

interface CreateBookModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onSave: (book: BookDTO) => void
  isLoading?: boolean
}

export function CreateBookModal({ open, onOpenChange, onSave, isLoading = false }: CreateBookModalProps) {
  const form = useForm<BookDTO>({
    defaultValues: {
      title: "",
      author: "",
      publisher: "",
      description: "",
      coverUrl: "",
      genres: [],
      isFeatured: false,
    },
  })

  const genreOptions = Object.values(BookDTOGenresEnum).map(genre => ({
    value: genre.toString(),
    label: getBookGenreLabel(genre),
  }))

  const onSubmit = (data: BookDTO) => {
    if (!data.title?.trim()) {
      form.setError('title', { message: 'Title is required' })
      return
    }
    if (!data.author?.trim()) {
      form.setError('author', { message: 'Author is required' })
      return
    }
    if (!data.publisher?.trim()) {
      form.setError('publisher', { message: 'Publisher is required' })
      return
    }
    if (!data.genres || data.genres.length === 0) {
      form.setError('genres', { message: 'At least one genre is required' })
      return
    }

    onSave(data)
    onOpenChange(false)
  }

  const handleGenreChange = (selectedGenres: string[]) => {
    const genreEnums = selectedGenres.map(genre => genre as BookDTOGenresEnum)
    form.setValue('genres', genreEnums)
  }

  const handleCancel = () => {
    form.reset()
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle>Create New Book</DialogTitle>
          <DialogDescription>
            Add a new book to the library. Fill in the required information below.
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Title</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter book title" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="author"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Author</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter author name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="publisher"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Publisher</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter publisher name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <textarea
                      placeholder="Enter book description"
                      className="min-h-[100px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="coverUrl"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cover URL</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter cover image URL" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="genres"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Genres</FormLabel>
                  <FormControl>
                    <MultiSelect
                      options={genreOptions}
                      onValueChange={handleGenreChange}
                      defaultValue={field.value?.map(genre => genre.toString()) || []}
                      placeholder="Select genres"
                      variant="default"
                      maxCount={5}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="isFeatured"
              render={({ field }) => (
                <FormItem className="flex flex-row items-start space-x-3 space-y-0">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                    />
                  </FormControl>
                  <div className="space-y-1 leading-none">
                    <FormLabel>Featured</FormLabel>
                    <FormDescription>
                      Mark as featured book
                    </FormDescription>
                  </div>
                </FormItem>
              )}
            />

            <DialogFooter>
              <Button type="button" variant="outline" onClick={handleCancel}>
                Cancel
              </Button>
              <Button type="submit" disabled={isLoading}>
                {isLoading ? "Creating..." : "Create Book"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
} 