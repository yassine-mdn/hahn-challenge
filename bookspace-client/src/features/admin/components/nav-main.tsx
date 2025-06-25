import {type LucideIcon} from "lucide-react"
import {
    SidebarGroup,
    SidebarGroupLabel,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
} from "@/features/admin/components/sidebar.tsx"
import {NavLink} from "react-router";

export function NavMain({
                            items,
                        }: {
    items: {
        title: string
        url: string
        icon?: LucideIcon
        isActive?: boolean
        items?: {
            title: string
            url: string
        }[]
    }[]
}) {
    return (
        <SidebarGroup>
            <SidebarGroupLabel>Options</SidebarGroupLabel>
            <SidebarMenu>
                {items.map((item) => (
                    <NavLink
                        key={item.title}
                        to={item.url}
                        className={({isActive}) => isActive ? "bg-accent rounded-md text-accent-foreground" : ""}
                    >
                        <SidebarMenuItem>
                            <SidebarMenuButton tooltip={item.title}>
                                {item.icon && <item.icon/>}
                                <span>{item.title}</span>
                            </SidebarMenuButton>
                        </SidebarMenuItem>
                    </NavLink>
                ))}
            </SidebarMenu>
        </SidebarGroup>
    )
}
