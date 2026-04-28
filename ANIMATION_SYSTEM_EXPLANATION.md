# Sistema de Animação do CPM Explicado

## Visão Geral

O CustomPlayerModels (CPM) utiliza um sistema de animação sofisticado que difere significativamente de editores tradicionais baseados em linha do tempo (timeline), como o Blockbench. Compreender essas diferenças é crucial para trabalhar de forma eficaz com as animações no CPM.

## Conceitos Principais

### 1. Duração da Animação e Sistema de Tempo

Diferente de editores de vídeo que trabalham com carimbos de tempo absolutos (segundos/milissegundos), o CPM usa um **sistema relativo baseado em frames** com as seguintes características principais:

- **Duração**: Medida em **ticks de jogo** (não milissegundos ou segundos)
  - 1 tick de jogo = 50ms (20 ticks por segundo)
  - Os valores de duração no CPM representam o comprimento total da animação em ticks
  - Exemplo: `duration = 1000` significa 1000 ticks = 50 segundos

- **Indexação de Frames**: As animações são compostas por frames discretos indexados de 0 a N-1
  - Progressão de tempo: `tempo_atual % duracao` fornece a posição atual no ciclo da animação
  - Cálculo do frame: `indice_do_frame = (tempo_atual % duracao) / duracao * total_de_frames`

### 2. Armazenamento de Keyframes (A Abordagem Única do CPM)

O CPM **não** armazena keyframes no sentido tradicional. Em vez disso:

#### A Classe Animation
Cada animação é representada por um objeto `Animation` contendo:
- `componentIDs[]`: Array de componentes do modelo (ossos/partes) sendo animados
- `psfs[][]`: Position/Scale/Function Splines - Curvas interpoladas para cada componente/atributo
- `show[][]`: Array booleano que controla a visibilidade por componente por frame
- `duration`: Comprimento total da animação em ticks
- `frames`: Número de frames discretos na animação
- `priority`: Prioridade de mistura (blending) da animação
- `add`: Define se esta animação é adicionada ou se substitui a pose base

#### Sistema de Interpolação
O CPM usa **interpolação por spline** em vez de keyframes lineares:
- Para cada componente animado e cada atributo (posição X/Y/Z, rotação X/Y/Z, etc.)
- O CPM cria um objeto `Interpolator` que mapeia valores de entrada (0 a frames-1) para valores de saída
- O interpolador utiliza técnicas como splines Catmull-Rom para curvas suaves
- Isso significa que você não define keyframes individuais — você define a curva inteira

### 3. O AnimationRegistry - O Contêiner de Animações do CPM

A classe `AnimationRegistry` é o núcleo central para todos os dados de animação:

#### Componentes Chave:
- **Mapa PoseToTriggers**: Mapeia poses (STANDING, WALKING, etc.) para listas de objetos `AnimationTrigger`
- **AnimationTrigger**: Vincula uma pose a um objeto `Animation` específico
- **Ações Nomeadas (Named Actions)**: Gatilhos de animação definíveis pelo usuário (como gestos)
- **Ações de Comando (Command Actions)**: Respostas de animação a comandos no chat
- **Poses Customizadas**: Poses definidas pelo usuário além das poses padrão do Minecraft

#### Como Funciona:
1. Quando uma entidade entra em uma pose (ex: começa a CAMINHAR/WALKING)
2. O registro procura por `poseToTriggers.get(WALKING_POSE)`
3. Para cada `AnimationTrigger` naquela lista, ele reproduz a `Animation` associada
4. Múltiplas animações podem ser reproduzidas simultaneamente em diferentes componentes (camadas)

### 4. Reprodução e Mistura (Blending) de Animação

O sistema de animação do CPM apresenta uma mistura sofisticada:

#### Modos de Animação
- `PLAYER`: Visão padrão em terceira pessoa
- `FIRST_PERSON`: Animações de visão em primeira pessoa
- `GUI`: Animações ao visualizar telas de interface (GUI)
- `HAND`: Animações de item/uso
- `SKULL`: Animações do bloco de cabeça (skull)

#### Sistema de Blending
- Animações com o mesmo modo se misturam com base na prioridade
- Animações de prioridade mais alta substituem as de prioridade mais baixa
- Animações podem ser configuradas para "adicionar" (sobrepor) ou "substituir" as poses base
- O cross-fading (transição suave) entre poses é tratado automaticamente

### 5. Dados de Frame vs. Keyframes Tradicionais

#### Linha do Tempo Tradicional (Blockbench/Editores de Vídeo):
- Armazena keyframes explícitos: {tempo: 0s, posição: (0,0,0)}, {tempo: 1s, posição: (1,0,0)}
- A interpolação acontece entre os keyframes armazenados
- Os usuários posicionam manualmente os keyframes em tempos específicos

#### Sistema do CPM:
- Armazena **pontos de controle de spline** para cada componente/atributo
- O construtor da `Animation` recebe arrays de dados brutos: `float[][][] data[componente][atributo][frame]`
- A partir desses dados, o CPM pré-calcula os interpoladores (`psfs`) durante a criação da animação
- Em tempo de execução, o CPM avalia essas splines na posição de tempo atual
- **Não há armazenamento explícito de keyframes** - apenas os pontos de controle da spline

### 6. Relação entre Duração e Frame

Fundamental para entender o sistema do CPM:

```
tempo_total_em_segundos = ticks_de_duracao / 20
tempo_por_frame_em_ticks = ticks_de_duracao / frames
tempo_por_frame_em_segundos = (ticks_de_duracao / 20) / frames
```

Exemplo:
- Uma animação com `duration = 1000` ticks (50 segundos) e `frames = 25`
- Cada frame representa 2 segundos de tempo de animação
- As splines são avaliadas nas posições 0, 1, 2, ..., 24
- Entre o frame 12 e 13, o CPM interpola suavemente usando as splines

### 7. Implicações Práticas para Usuários

#### O que isso significa para a criação de animações:
1. **Você não coloca keyframes em uma linha do tempo** - você define curvas de movimento
2. **A suavidade vem da resolução da spline** - mais frames = interpolação mais suave
3. **A duração controla a velocidade** - maior duração = animação mais lenta (mesmo movimento em mais tempo)
4. **A contagem de frames controla a precisão** - mais frames = mais pontos de controle para movimentos complexos
5. **A edição é baseada em curvas** - você ajusta a spline subjacente, não pontos individuais

#### Diferenças em relação ao Blockbench:
| Recurso | Blockbench | CPM |
|---------|------------|-----|
| Unidade Primária | Segundos/Milissegundos | Ticks de Jogo (50ms) |
| Armazenamento de Keyframe | Pares explícitos de tempo/valor | Pontos de controle de spline |
| Interpolação | Linear/Bezier entre keyframes | Splines pré-calculadas |
| Visualização da Timeline | Escala de tempo absoluta | Indexação de frames relativa |
| Método de Edição | Posicionar/mover keyframes | Ajustar curvas de animação |
| Blending (Mistura) | Trilhas baseadas em camadas | Mistura baseada em prioridade |
| Sistema de Poses | Hierarquia de ossos | Animações disparadas por poses |

### 8. Trabalhando Dentro das Limitações do CPM

Para criar animações eficazes no CPM:

1. **"Underpose" seu movimento**: Como as animações são disparadas por poses, projete movimentos que se encaixem naturalmente no sistema de poses do Minecraft (STANDING, WALKING, etc.)

2. **Use a Duração Apropriada**: 
   - Gestos rápidos: 20-40 ticks (1-2 segundos)
   - Ciclos de caminhada: 40-80 ticks (2-4 segundos) por passo
   - Animações complexas: 100+ ticks (5+ segundos)

3. **Equilibre a Contagem de Frames**: 
   - Poucos frames: movimento travado (jerky)
   - Muitos frames: difícil de editar, arquivo maior
   - Comece com 16-32 frames para a maioria das animações

4. **Aproveite o Blending**: Crie animações base (idle) e sobreponha ações específicas (acenar, apontar)

5. **Use Ações Nomeadas**: Para clipes de animação reutilizáveis que podem ser acionados por gestos ou comandos

### 9. O Papel do Editor

O editor do CPM (classes `Editor` e `EditorAnim`) preenche a lacuna entre o pensamento tradicional e o sistema interno do CPM:

- **EditorAnim**: Armazena frames como objetos `AnimFrame` contendo dados de transformação brutos
- **Ao Salvar**: Converte esses frames em dados de spline para a classe interna `Animation`
- **Ao Carregar**: Reconstrói os dados da spline de volta em frames editáveis
- **Isso explica por que o editor parece uma linha do tempo** - ele está convertendo entre representações

## Exemplo Prático

Criando uma animação simples de aceno:

1. **No Editor do CPM**:
   - Crie uma nova animação, defina a duração = 40 ticks (2 segundos)
   - Crie 8 frames (um a cada 5 ticks = 0,25 segundos)
   - Anime a rotação Y do braço de 0° a 45° e de volta ao longo dos frames
   - O editor armazena esses 8 keyframes e gera splines para um movimento suave

2. **Internamente no CPM**:
   - Cria uma `Animation` com:
     - `duration = 40`
     - `frames = 8` 
     - Splines que interpolam suavemente entre as suas 8 poses de keyframe
   - Em tempo de execução:
     - Tempo 0-10 ticks: avalia a spline para o primeiro quarto do movimento
     - Tempo 10-20 ticks: avalia a spline para o segundo quarto
     - etc.

3. **Resultado**: Movimento de aceno suave que entra em loop perfeitamente a cada 2 segundos

## Conclusão

O sistema de animação do CPM é poderoso, mas requer uma mudança de mentalidade da edição tradicional baseada em keyframes para o **design de animação baseado em curvas**. O sistema prioriza:
- Movimento suave e interpolado
- Contextos de animação disparados por poses
- Avaliação eficiente em tempo de execução através de splines pré-calculadas
- Mistura e camadas flexíveis

Uma vez que você entende que está projetando curvas de movimento em vez de apenas colocar keyframes em uma linha do tempo, a abordagem do CPM torna-se intuitiva e altamente eficaz para animações específicas de Minecraft.