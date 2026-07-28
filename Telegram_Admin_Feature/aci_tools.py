import os
import shutil

class ACITools:
    """
    Agent-Computer Interface (ACI) Tools.
    Provides safe, controlled wrapper methods for files, directories,
    and storage operations, preventing destructive shell commands.
    """
    
    @staticmethod
    def list_dir_safe(path):
        """
        Safely list directory contents, excluding system and hidden folders.
        """
        if not os.path.exists(path):
            return {"error": f"Path '{path}' does not exist."}
            
        exclude = {'$recycle.bin', 'system volume information', '.git', '.agents'}
        try:
            items = os.listdir(path)
            result = {"directories": [], "files": []}
            for item in items:
                if item.lower() in exclude:
                    continue
                item_path = os.path.join(path, item)
                if os.path.isdir(item_path):
                    result["directories"].append(item)
                else:
                    result["files"].append(item)
            return result
        except Exception as e:
            return {"error": str(e)}

    @staticmethod
    def check_disk_space(drive_letter):
        """
        Check and report free and total space on a given drive.
        """
        path = f"{drive_letter}:\\"
        if not os.path.exists(path):
            return {"error": f"Drive '{drive_letter}' is not accessible."}
        try:
            total, used, free = shutil.disk_usage(path)
            return {
                "drive": drive_letter,
                "total_gb": round(total / (1024**3), 2),
                "used_gb": round(used / (1024**3), 2),
                "free_gb": round(free / (1024**3), 2)
            }
        except Exception as e:
            return {"error": str(e)}

    @staticmethod
    def safe_move_file(src, dest):
        """
        Safely move a file, avoiding overwriting active configuration directories.
        """
        if not os.path.exists(src):
            return {"error": f"Source '{src}' does not exist."}
        if os.path.isdir(src):
            return {"error": "Moving directories is blocked for safety. Only files can be moved."}
            
        # Security check: Block moving system files
        blocked_extensions = {'.sys', '.dll', '.exe', '.bat', '.cmd'}
        ext = os.path.splitext(src.lower())[1]
        if ext in blocked_extensions:
            return {"error": f"Moving files with extension '{ext}' is blocked for safety."}
            
        try:
            # Create destination folder if not exists
            dest_dir = os.path.dirname(dest)
            if dest_dir and not os.path.exists(dest_dir):
                os.makedirs(dest_dir)
                
            shutil.move(src, dest)
            return {"status": "success", "message": f"Moved {os.path.basename(src)} to {dest}"}
        except Exception as e:
            return {"error": str(e)}

if __name__ == "__main__":
    # Self-test
    print("Testing list_dir_safe on current path:")
    print(ACITools.list_dir_safe("."))
