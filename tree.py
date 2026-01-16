import os

def generate_text_tree(startpath):
    # Dossiers à ignorer pour ne pas polluer le résumé
    ignore_dirs = {'.git', '.idea', '.gradle', 'build', '__pycache__', 'mipmap', 'drawable', 'values', 'xml'}
    
    output = "com.travel.travelshare\n"
    
    # On cible le dossier racine du code Java
    java_root = os.path.join(startpath, "app", "src", "main", "java")
    
    # On descend jusqu'au package principal pour commencer l'arbre proprement
    actual_root = java_root
    for folder in ["com", "travel", "travelshare"]:
        actual_root = os.path.join(actual_root, folder)

    if not os.path.exists(actual_root):
        return "Erreur : Chemin introuvable. Lancez le script à la racine du projet."

    for root, dirs, files in os.walk(actual_root):
        # Filtrage des dossiers
        dirs[:] = [d for d in dirs if d not in ignore_dirs]
        
        level = root.replace(actual_root, '').count(os.sep)
        indent = '│   ' * (level)
        sub_indent = '│   ' * (level + 1)
        
        # Nom du dossier actuel
        if root != actual_root:
            output += f"{indent}├── {os.path.basename(root)}/\n"
        
        # Liste des fichiers Java
        java_files = [f for f in files if f.endswith('.java')]
        for i, f in enumerate(java_files):
            connector = "└── " if i == len(java_files) - 1 and not dirs else "├── "
            # Optionnel : on ne met que les fichiers importants ou on limite par dossier
            if level == 0 or len(java_files) < 10: 
                output += f"{sub_indent}{connector}{f}\n"
            else:
                output += f"{sub_indent}└── ({len(java_files)} fichiers Java...)\n"
                break

    return output

# --- Exécution ---
if __name__ == "__main__":
    # On récupère le dossier courant
    project_path = os.getcwd()
    tree_text = generate_text_tree(project_path)
    
    print("\nStructure du projet pour LaTeX (Verbatim) :\n")
    print(tree_text)
    
    # Sauvegarde dans un fichier texte pour plus de facilité
    with open("architecture.txt", "w", encoding="utf-8") as f:
        f.write(tree_text)
    print("\nFichier 'architecture.txt' créé.")
