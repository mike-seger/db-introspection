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
    themeCSS: customStyles,
    themeVariables: {
        fontFamily: 'helvetica-light, arial-light, sans-serif'
    },
    theme: 'default'
})

async function renderMermaidDiagram(url, target) {
  const response = await fetch(url);
  const mermaidDiagramText = await response.text();
  const { svg } =  await mermaid.render(target.id+"SVG", mermaidDiagramText);
  target.innerHTML = svg
}

await renderMermaidDiagram('/mermaid/erd?markdown=false', erDiagramDisplay)


