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

document.getElementById('copyBtn').addEventListener('click', () => {
  const output = document.getElementById('output');
  const text = Array.from(output.querySelectorAll('p'))
    .map(p => p.innerText)
    .join('\n');
  navigator.clipboard.writeText(text)
    .then(() => alert('Sentences copied to clipboard!'))
    .catch(err => alert('Copy error: ' + err));
});

document.getElementById('saveBtn').addEventListener('click', () => {
  const phrases = Array.from(document.getElementById('output').querySelectorAll('p'))
    .map(p => p.innerText);

  fetch('/api/save', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ phrases })
  })
    .then(response => {
      if (!response.ok) throw new Error('Save error');
      return response.text();
    })
    .then(msg => alert(msg))
    .catch(err => alert('Error: ' + err.message));
});
