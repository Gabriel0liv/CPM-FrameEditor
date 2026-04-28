# CPM Timeline Addon

Addon para CustomPlayerModels que adiciona uma interface de timeline para edição de animações, similar ao Blockbench.

## Estrutura do Projeto

Este projeto foi reestruturado para ter o addon na raiz (não mais em `addons/cpm-timeline`). O source do CPM é incluído via `srcDir` no `build.gradle` apenas para compilação — não é empacotado no JAR final.

```
addonCPM/
├── src/main/java/com/tom/cpm/timeline/    # Código do addon
├── Referencia_CPM/                         # Source do CPM (apenas referência)
├── build.gradle                            # Build configurado para incluir CPM shared
└── README.md
```

## Pré-requisitos

- Java 17+
- Gradle 8.8 (incluído via wrapper)
- CustomPlayerModels source em `Referencia_CPM/CustomPlayerModels-master/`

## Build

### Limpar cache e buildar do zero

```bash
.\gradlew.bat clean build --no-daemon
```

### Build rápido (após primeira compilação)

```bash
.\gradlew.bat build
```

### Apenas compilar (sem gerar JAR)

```bash
.\gradlew.bat compileJava
```

## Problemas Conhecidos

### "Entry cpm_timeline.mixins.json is a duplicate"

Se você ver este erro, o Gradle daemon está com cache corrompido. Solução:

```bash
.\gradlew.bat clean build --no-daemon
```

### Erros do Language Server no IDE

O VS Code/IntelliJ pode mostrar erros de import (`com.tom.cpl cannot be resolved`) mesmo quando o build funciona. Isso é porque o IDE não sabe dos `srcDir` extras. Para corrigir:

**Eclipse:**
```bash
.\gradlew.bat eclipse
```

**IntelliJ:**
```bash
.\gradlew.bat idea
```

Depois reabra o projeto no IDE.

### "platform-shared" compilation errors

Se você ver erros de `CustomPlayerModelsClient`, `GuiImpl`, etc., significa que o `build.gradle` está incluindo `platform-shared` por engano. Verifique que o `sourceSets.main.java` tem apenas:

```gradle
srcDir 'src/main/java'
srcDir 'Referencia_CPM/CustomPlayerModels-master/CustomPlayerModels/src/shared/java'
```

**NÃO** deve incluir `platform-shared`.

## Output

O JAR compilado estará em:
```
build/libs/cpm-timeline-1.0.0.jar
```

## Instalação

1. Compile o addon (veja acima)
2. Copie `build/libs/cpm-timeline-1.0.0.jar` para a pasta `mods` do Minecraft
3. Certifique-se de que o CustomPlayerModels também está instalado
4. O addon será carregado automaticamente após o CPM

## Funcionalidades e Uso

### 1. Timeline de Animação
*   **Local:** Aba **Animation** do editor CPM, na parte inferior.
*   **Recursos:**
    *   **Scrubbing:** Clique e arraste na área da timeline para pré-visualizar a animação em tempo real.
    *   **Reordenar Keyframes:** Clique e arraste os diamantes (keyframes) para mudar sua posição na sequência.
    *   **Indicador de Tempo:** Linha vermelha (playhead) mostra a posição atual e marcadores de segundos facilitam o timing.

### 2. Configurações e Cores
*   **Local:** Painel de controle de animação (junto aos botões de Play/Stop).
*   **Recursos:**
    *   **Checkbox [T]:** Alterna a visibilidade da timeline.
    *   **Seletor de Cor:** Define uma cor personalizada para o keyframe selecionado. As cores são salvas no arquivo do projeto.

### 3. Seleção em Bloco (Shift-Selection)
*   **Local:** Árvore de modelos (Tree Panel) ou Visualização 3D.
*   **Uso:** Selecione um elemento, segure **Shift** e clique em outro para selecionar todos os elementos no intervalo.

### 4. Ferramentas de Pivot (Align Pivot)
*   **Local:** Menu de contexto (botão direito) em qualquer elemento -> **Align Pivot**.
*   **Opções:** Center, Top, Bottom, Left, Right, Front, Back.
*   **Efeito:** Move o ponto de articulação para a posição escolhida mantendo o bloco visualmente parado (ajusta posição e offset simultaneamente).

### 5. Agrupamento Rápido (Group Selected)
*   **Local:** Rodapé do painel da árvore, ícone de pasta (**📁**).
*   **Uso:** Selecione múltiplos elementos e clique no ícone para movê-los para um novo grupo. O pivot do grupo é calculado automaticamente no centro dos itens selecionados.

## Desenvolvimento

### Estrutura do Código

- `CPMTimelineAddon.java` - Classe principal do mod (@Mod)
- `TimelinePanel.java` - Painel principal da timeline com lista de frames
- `TimelineAnimPanel.java` - Wrapper que integra a timeline no editor
- `mixin/EditorGuiMixin.java` - Mixin que injeta a timeline no `EditorGui.initAnimPanel()`

### Como Funciona

O addon usa:
1. **Reflection** para acessar `Editor.selectedAnim` e `EditorAnim.getFrames()`
2. **Mixin** para injetar o painel da timeline no `EditorGui` após `initAnimPanel()`
3. **CPM GUI API** (`Panel`, `Button`, `Label`, `ScrollPanel`) para a interface

### Debugging

Para ver logs do addon:
```
[CPM Timeline] Addon loaded!
[CPM Timeline] Timeline panel injected successfully
```

Se não aparecer "injected successfully", o mixin falhou. Verifique:
- `cpm_timeline.mixins.json` está em `src/main/resources/`
- O manifest do JAR tem `MixinConfigs: cpm_timeline.mixins.json`

## Licença

MIT
