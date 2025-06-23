import {Separator} from "@/components/ui/separator.tsx";

const Footer = () => {
    return (
        <footer className={" h-86 w-screen bg-card "}>
            <div className="container h-full mx-auto px-4 py-8 flex flex-col ">
                <div className="grid grid-cols-1 gap-8 md:grid-cols-4 grow">
                    {/* Company Info */}
                    <div className="space-y-3">
                        <div className="flex items-center space-x-2">
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 16 16"
                                className="w-6 h-6"
                                fill="none"
                            >
                                <g clipPath="url(#a)" >
                                    <path
                                        fill="white"
                                        fillRule="evenodd"
                                        d="M8 1.333H2v8.889h6v4.445h6V5.778H8z"
                                        clipRule="evenodd"
                                    />
                                </g>
                                <defs>
                                    <clipPath id="a">
                                        <path fill="white" d="M0 0h16v16H0z" />
                                    </clipPath>
                                </defs>
                            </svg>
                            <span className={"text-2xl font-bold hidden md:block"}>BookSpace</span>
                        </div>
                        <p className="text-sm text-muted-foreground">A place holder Footer added for visual consistency.</p>
                    </div>

                    {/* Product Links */}
                    <div className="space-y-3">
                        <h4 className="text-sm font-semibold">Product</h4>
                        <ul className="space-y-2 text-sm">
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Features
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Pricing
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Documentation
                                </a>
                            </li>
                        </ul>
                    </div>

                    {/* Company Links */}
                    <div className="space-y-3">
                        <h4 className="text-sm font-semibold">Company</h4>
                        <ul className="space-y-2 text-sm">
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    About
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Blog
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Careers
                                </a>
                            </li>
                        </ul>
                    </div>

                    {/* Support Links */}
                    <div className="space-y-3">
                        <h4 className="text-sm font-semibold">Support</h4>
                        <ul className="space-y-2 text-sm">
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Help Center
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Contact
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                                    Status
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <Separator className="my-6" />

                {/* Bottom Section */}
                <div className="flex flex-col items-center justify-between gap-4 md:flex-row">
                    <p className="text-sm text-muted-foreground">© 2024 Company Name. All rights reserved.</p>
                    <div className="flex gap-6 text-sm">
                        <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                            Privacy Policy
                        </a>
                        <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                            Terms of Service
                        </a>
                        <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
                            Cookie Policy
                        </a>
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default Footer;