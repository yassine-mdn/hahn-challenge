import {SidebarInset, SidebarProvider, SidebarTrigger} from "@/features/admin/components/sidebar.tsx";
import {AppSidebar} from '@/features/admin/components/app-sidebar';
import {Separator} from '@/components/ui/separator';
import {Outlet, useLocation} from "react-router";
import {Breadcrumb, BreadcrumbItem, BreadcrumbList, BreadcrumbPage} from "@/components/ui/breadcrumb";


const AdminLayout = () => {
    const location = useLocation();
    
    // Determine the current page based on the URL path
    const getCurrentPage = () => {
        const path = location.pathname;
        if (path.includes('/admin/books')) {
            return 'Books';
        } else if (path.includes('/admin/users')) {
            return 'Users';
        }
        return 'Home';
    };

    return (
        <SidebarProvider>
            <AppSidebar/>
            <SidebarInset>
                <header
                    className="flex h-16 shrink-0 items-center gap-2 transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12 border-b">
                    <div className="flex items-center gap-2 px-4">
                        <SidebarTrigger className="-ml-1"/>
                        <Separator
                            orientation="vertical"
                            className="mr-2 data-[orientation=vertical]:h-4"
                        />
                        <Breadcrumb>
                            <BreadcrumbList>
                                <BreadcrumbItem>
                                    <BreadcrumbPage>{getCurrentPage()}</BreadcrumbPage>
                                </BreadcrumbItem>
                            </BreadcrumbList>
                        </Breadcrumb>
                    </div>
                </header>
                <div className="flex flex-1 flex-col mx-auto container  gap-4 p-4 pt-0">
                    <Outlet/>
                </div>
            </SidebarInset>
        </SidebarProvider>
    );
};

export default AdminLayout;