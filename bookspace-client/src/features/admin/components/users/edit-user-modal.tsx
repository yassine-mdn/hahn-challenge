import { useEffect } from "react"
import { useForm } from "react-hook-form"
import type { UserDTO } from "@/types/user-dto"
import { UserDTORoleEnum } from "@/types/user-dto"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
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
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"

interface EditUserModalProps {
  user: UserDTO | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onSave: (user: UserDTO) => void
  isLoading?: boolean
}

export function EditUserModal({ user, open, onOpenChange, onSave, isLoading = false }: EditUserModalProps) {
  const form = useForm<UserDTO>({
    defaultValues: {
      id: undefined,
      username: "",
      email: "",
      password: "",
      role: UserDTORoleEnum.USER,
    },
  })

  useEffect(() => {
    if (user) {
      form.reset({
        id: user.id,
        username: user.username || "",
        email: user.email || "",
        password: "", // Don't populate password for security
        role: user.role || UserDTORoleEnum.USER,
      })
    }
  }, [user, form])

  useEffect(() => {
    if (!open) {
      form.reset()
    }
  }, [open, form])

  const onSubmit = (data: UserDTO) => {
    if (!data.username?.trim()) {
      form.setError('username', { message: 'Username is required' })
      return
    }
    if (!data.email?.trim()) {
      form.setError('email', { message: 'Email is required' })
      return
    }

    // Only include password if it was changed
    const userData = { ...data }
    if (!userData.password?.trim()) {
      delete userData.password
    }

    onSave(userData)
    onOpenChange(false)
  }

  const handleCancel = () => {
    form.reset()
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>Edit User</DialogTitle>
          <DialogDescription>
            Make changes to the user information here. Click save when you're done.
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Username</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter username" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input type="email" placeholder="Enter email address" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input 
                      type="password" 
                      placeholder="Leave blank to keep current password" 
                      {...field} 
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="role"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Role</FormLabel>
                  <Select onValueChange={field.onChange} defaultValue={field.value}>
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a role" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value={UserDTORoleEnum.USER}>User</SelectItem>
                      <SelectItem value={UserDTORoleEnum.ADMIN}>Admin</SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter>
              <Button type="button" variant="outline" onClick={handleCancel} disabled={isLoading}>
                Cancel
              </Button>
              <Button type="submit" disabled={isLoading}>
                {isLoading ? "Saving..." : "Save Changes"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
} 