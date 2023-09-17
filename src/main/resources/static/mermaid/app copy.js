import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.js';

const container = document.getElementById('erDiagramDisplay');

const customStyles = `
    .er.entityBox { fill: #abddf3; stroke: #dddddd; }
    .er.attributeBoxEven { fill: #dddddd; stroke: #dddddd; }
    .er.attributeBoxOdd { stroke: #dddddd; }
    .er.entityLabel { stroke: #555555; }
`

mermaid.initialize({
    startOnLoad: true,
    'themeCSS': customStyles,
    theme: 'default',
    callback:function(id){
        console.log(id,' rendered');
    }
})

//async function fetchText(url) {
//    const response = await fetch(url);
//    return await response.text();
//}

//
//container.innerText = await fetchText('/mermaid/erd?markdown=false')

async function renderDiagram() {
    //const data = await fetchText('/mermaid/erd?markdown=false')
    //const {svg, bindFunctions} = await mermaid.render(data, atob(container.innerText));
    const data = `
sequenceDiagram
    participant Alice
    participant Bob
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts <br/>prevail...
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob-->>John: Jolly good!
`
    mermaid.render('erDiagramDisplay', data).then((svgData) => {
        container.innerHtml = svgData;
        console.log(svgData)
    })
    .catch((error) => console.log(error))

}

renderDiagram()

//(function () {
//    const {svg, bindFunctions} = await mermaid.render(data, atob(container.innerText));
//})()

/*
window.onload = function() {
    const customStyles = `
        .er.entityBox { fill: #abddf3; stroke: #dddddd; }
        .er.attributeBoxEven { fill: #dddddd; stroke: #dddddd; }
        .er.attributeBoxOdd { stroke: #dddddd; }
        .er.entityLabel { stroke: #555555; }
    `

    mermaid.initialize({
        startOnLoad: false,
        'themeCSS': customStyles,
        theme: 'default',
    })
    fetch('/mermaid/erd?markdown=false')
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.text();
        })
        .then(data => {
            mermaid.render('theGraph', data, function(svgCode){
                container.innerHTML = svgCode;
            });
        })
        .catch(error => {
            console.error('There was a problem fetching the ER diagram:', error);
        });
}*/

let isDragging = false;
let previousX, previousY;

const body = document.body;

const originalWidth = container.offsetWidth;
const originalHeight = container.offsetHeight;

let scale = 1;
const scaleIncrement = 0.1;

document.addEventListener('wheel', function(event) {
    if (event.ctrlKey) {
        event.preventDefault();

        if (event.deltaY < 0) {
            scale += scaleIncrement;
        } else {
            scale -= scaleIncrement;
        }

        // Set the new dimensions
        container.style.width = `${originalWidth * scale}px`;
        container.style.height = `${originalHeight * scale}px`;

        // Apply the scale transformation
        container.style.transform = `scale(${scale})`;
        container.style.transformOrigin = 'center top';
    }
});

container.addEventListener('mousedown', function(e) {
    isDragging = true;
    previousX = e.clientX;
    previousY = e.clientY;
});

window.addEventListener('mousemove', function(e) {
    if (!isDragging) return;

    let deltaX = previousX - e.clientX;
    let deltaY = previousY - e.clientY;

    window.scrollBy(deltaX, deltaY);

    previousX = e.clientX;
    previousY = e.clientY;

    // Prevent default to avoid any text selection during drag
    e.preventDefault();
});

window.addEventListener('mouseup', function() {
    isDragging = false;
});

window.addEventListener('mouseout', function(e) {
    if (e.relatedTarget === null) isDragging = false; // Stop dragging if the mouse leaves the viewport
});
