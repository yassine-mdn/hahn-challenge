import type {SVGProps} from "react";

const Logo = (props: SVGProps<SVGSVGElement>) => (
    <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 16 16"
        {...props}
    >
        <path fill="#fff" d="M8 1.333H2v8.889h6v4.445h6V5.778H8z" />
    </svg>
)

export default Logo;