async function cadastrar() {
  const nome  = document.getElementById('nome').value.trim();
  const email = document.getElementById('email').value.trim();
  const senha = document.getElementById('senha').value;
  const tipo  = document.getElementById('tipo').value;
  const res   = document.getElementById('result');

  if (!nome || !email || !senha) {
    res.className = 'result err';
    res.style.display = 'block';
    res.textContent = 'Preencha todos os campos.';
    return;
  }

  res.className = 'result';
  res.style.display = 'block';
  res.textContent = 'Enviando...';

  const params = new URLSearchParams();
  params.append('nome', nome);
  params.append('email', email);
  params.append('senha', senha);
  params.append('tipo', tipo);

  try {
    const r = await fetch('http://localhost:8080/api/usuario', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });

    const text = await r.text();

    if (r.ok || r.status === 201) {
      res.className = 'result ok';
      res.textContent = 'Conta criada com sucesso!';
    } else {
      res.className = 'result err';
      res.textContent = 'Erro ' + r.status + ': ' + text;
    }
  } catch (e) {
    res.className = 'result err';
    res.textContent = 'Falha na conexão: ' + e.message;
  }
}