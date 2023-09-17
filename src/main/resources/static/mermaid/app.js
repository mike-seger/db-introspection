import mermaid from "https://cdn.jsdelivr.net/npm/mermaid@10.4.0/dist/mermaid.esm.min.mjs"

const erDiagramDisplay = document.getElementById('erDiagramDisplay')

const customStyles = `
    .er.entityBox { fill: #abddf3; stroke: #dddddd; }
    .er.attributeBoxEven { fill: #dddddd; stroke: #dddddd; }
    .er.attributeBoxOdd { stroke: #dddddd; }
    .er.entityLabel { stroke: #555555; }
`

mermaid.initialize({
    startOnLoad: true,
    'themeCSS': customStyles,
    theme: 'default'
})

async function renderMermaidDiagram() {
  const response = await fetch('/mermaid/erd?markdown=false');
  const mermaidDiagramText = await response.text();
  const { svg } =  await mermaid.render("erDiagramDisplaySVG", mermaidDiagramText);
  erDiagramDisplay.innerHTML = svg
}

await renderMermaidDiagram()


let isDragging = false
let previousX, previousY

const body = document.body

const originalWidth = erDiagramDisplay.offsetWidth
const originalHeight = erDiagramDisplay.offsetHeight

let scale = 1
const scaleIncrement = 0.1

document.addEventListener('wheel', function(event) {
    if (event.ctrlKey) {
        event.preventDefault()

        if (event.deltaY < 0) scale += scaleIncrement
        else scale -= scaleIncrement
        if(scale < scaleIncrement) scale = scaleIncrement

        erDiagramDisplay.style.width = `${originalWidth * scale}px`
        erDiagramDisplay.style.height = `${originalHeight * scale}px`

        erDiagramDisplay.style.transform = `scale(${scale})`
        erDiagramDisplay.style.transformOrigin = 'left top'
    }
})

erDiagramDisplay.addEventListener('mousedown', function(e) {
    isDragging = true
    previousX = e.clientX
    previousY = e.clientY
})

window.addEventListener('mousemove', function(e) {
    if (!isDragging) return

    let deltaX = previousX - e.clientX
    let deltaY = previousY - e.clientY

    window.scrollBy(deltaX, deltaY)

    previousX = e.clientX
    previousY = e.clientY

    e.preventDefault()
})

window.addEventListener('mouseup', function() {
    isDragging = false
})

window.addEventListener('mouseout', function(e) {
    if (e.relatedTarget === null) isDragging = false
})
