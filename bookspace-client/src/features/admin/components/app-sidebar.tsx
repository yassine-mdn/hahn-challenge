import * as React from "react"
import {BookOpen, UsersRound,} from "lucide-react"

import {NavMain} from "@/features/admin/components/nav-main.tsx"
import {NavUser} from "@/features/admin/components/nav-user.tsx"
import {TeamSwitcher} from "@/features/admin/components/team-switcher.tsx"
import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarHeader,
    SidebarRail,
} from "@/features/admin/components/sidebar.tsx"
import Logo from "@/components/ui/logo.tsx";

// This is sample data.
const data = {
    teams: {
        name: "BookSpace Admin",
        logo: Logo
    },
    navMain: [
        {
            title: "Books",
            url: "books",
            icon: BookOpen,

        },
        {
            title: "Users",
            url: "users",
            icon: UsersRound,

        },
    ]
}

export function AppSidebar({...props}: React.ComponentProps<typeof Sidebar>) {
    return (
        <Sidebar collapsible="icon" {...props}>
            <SidebarHeader>
                <TeamSwitcher teams={data.teams}/>
            </SidebarHeader>
            <SidebarContent>
                <NavMain items={data.navMain}/>
            </SidebarContent>
            <SidebarFooter>
                <NavUser/>
            </SidebarFooter>
            <SidebarRail/>
        </Sidebar>
    )
}
