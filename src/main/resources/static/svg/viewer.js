document.addEventListener("DOMContentLoaded", function () {
  const viewerElement = document.getElementById('viewer');
  const svgURL = '/svg/diagram1.svg';

  let translateX = 0;
  let translateY = 0;
  let scale = 1; // Added scale for zooming.
  let scaleX;
  let scaleY;

  fetch(svgURL)
      .then(response => response.text())
      .then(data => {
          viewerElement.innerHTML = data;
          const svgElement = viewerElement.querySelector('svg');
          const svgContent = svgElement.innerHTML;

          const viewportWidth = window.innerWidth;
          const viewportHeight = window.innerHeight;

          const svgWidth = svgElement.viewBox.baseVal.width;
          const svgHeight = svgElement.viewBox.baseVal.height;

          scaleX = viewportWidth / svgWidth;
          scaleY = viewportHeight / svgHeight;

          svgElement.style.width = `${viewportWidth}px`;
          svgElement.style.height = `${viewportHeight}px`;

          const gElement = document.createElementNS('http://www.w3.org/2000/svg', 'g');
          gElement.innerHTML = svgContent;
          svgElement.innerHTML = '';
          svgElement.appendChild(gElement);

          viewerElement.addEventListener('mousedown', startPanning);
          viewerElement.addEventListener('wheel', zoom); // Added wheel event listener for zooming.
      })
      .catch(error => {
          console.error('Error fetching SVG:', error);
      });

  function startPanning(event) {
      const startX = event.clientX;
      const startY = event.clientY;
      const startTranslateX = translateX;
      const startTranslateY = translateY;

      function pan(event) {
          const dx = (event.clientX - startX) * 3 /scaleX;
          const dy = (event.clientY - startY) * 3 /scaleX;

          translateX = startTranslateX + dx;
          translateY = startTranslateY + dy;

          updateTransform(); // Updated this to use a common function for setting transform.
      }

      function stopPanning() {
          viewerElement.removeEventListener('mousemove', pan);
          viewerElement.removeEventListener('mouseup', stopPanning);
          viewerElement.removeEventListener('mouseleave', stopPanning);
      }

      viewerElement.addEventListener('mousemove', pan);
      viewerElement.addEventListener('mouseup', stopPanning);
      viewerElement.addEventListener('mouseleave', stopPanning);
  }

  function zoom(event) {
      event.preventDefault();

      const zoomFactor = event.deltaY < 0 ? 1.1 : 0.9;
      scale *= zoomFactor;
      scale = Math.max(0.1, Math.min(scale, 5)); // Constraint scale between 0.1 and 5.

      updateTransform();
  }

  function updateTransform() {
      const gElement = viewerElement.querySelector('g');
      gElement.setAttribute('transform', `translate(${translateX} ${translateY}) scale(${scale})`);
  }
});
