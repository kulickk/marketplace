const TemplateSVG = ({children, className, onClick}) => {
    return(
        <div className={className} onClick={onClick}>
            {children}
        </div>
    );
};

export default TemplateSVG;