import TemplateSVG from "./TemplateSVG";

const SelectionMenuSVG = () => {
    return(
        <TemplateSVG>
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M0 0H18V4H0V0Z" fill="white"/>
                <path d="M0 7H18V11H0V7Z" fill="white"/>
                <path d="M0 14H18V18H0V14Z" fill="white"/>
            </svg>
        </TemplateSVG>
    );
};

export default SelectionMenuSVG;