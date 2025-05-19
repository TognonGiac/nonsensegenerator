function analyzeText() {
  const input = document.getElementById("inputText").value;

  if (!input.trim()) {
    document.getElementById("output").innerText = "Inserisci una frase prima di analizzare!";
    return;
  }

  // Qui in futuro chiamerai l'API Google NLP
  document.getElementById("output").innerHTML = `
    <p><strong>Frase analizzata:</strong> ${input}</p>
    <p><em>[Qui verranno mostrati i risultati dell'analisi]</em></p>
  `;
}
