import {ChevronsUpDown, LogOut,} from "lucide-react"
import {toast} from "sonner"
import {useAuth} from "@/features/auth/AuthContext"
import {useMutation} from "@tanstack/react-query"

import {Avatar, AvatarFallback, AvatarImage,} from "@/components/ui/avatar.tsx"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {SidebarMenu, SidebarMenuButton, SidebarMenuItem, useSidebar,} from "@/features/admin/components/sidebar.tsx"

export function NavUser() {
  const { isMobile } = useSidebar()
  const { user,logout } = useAuth()



  const logoutMutation = useMutation({
    mutationFn: async () => {
      logout()
    },
    onSuccess: () => {
      toast("Logged out successfully.")
    },
  })

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <SidebarMenuButton
              size="lg"
              className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
            >
              <Avatar className="h-8 w-8 rounded-lg">
                <AvatarImage src={`https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${user}`} alt={user|| "Admin"} />
                <AvatarFallback className="rounded-lg">CN</AvatarFallback>
              </Avatar>
              <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate font-medium">{user || "Admin"}</span>
                <span className="truncate text-xs">{"admin@bookspace.com"}</span>
              </div>
              <ChevronsUpDown className="ml-auto size-4" />
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="w-(--radix-dropdown-menu-trigger-width) min-w-56 rounded-lg"
            side={isMobile ? "bottom" : "right"}
            align="end"
            sideOffset={4}
          >
            <DropdownMenuLabel className="p-0 font-normal">
              <div className="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                <Avatar className="h-8 w-8 rounded-lg">
                  <AvatarImage src={`https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${user}`} alt={user || "Admin"} />
                  <AvatarFallback className="rounded-lg">YM</AvatarFallback>
                </Avatar>
                <div className="grid flex-1 text-left text-sm leading-tight">
                  <span className="truncate font-medium">{user || "Admin"}</span>
                  <span className="truncate text-xs">{"admin@bookspace.com"}</span>
                </div>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={() => logoutMutation.mutate()}>
              <LogOut />
              Log out
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  )
}
