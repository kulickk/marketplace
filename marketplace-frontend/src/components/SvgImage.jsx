'use client';
import { useEffect, useState } from 'react';

const SvgIcon = ({ src, className }) => {
  const [svgContent, setSvgContent] = useState('');

  useEffect(() => {
    fetch(src)
      .then((res) => res.text())
      .then((text) => {
        const modifiedSvg = text.replace(/fill="[^"]*"/g, 'fill="currentColor"');
        setSvgContent(modifiedSvg);
      });
  }, [src]);

  return (
    <div 
      className={className}
      dangerouslySetInnerHTML={{ __html: svgContent }} 
    />
  );
};

export default SvgIcon;