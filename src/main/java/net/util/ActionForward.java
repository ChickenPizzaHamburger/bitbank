package net.util;

	public class ActionForward {
	    private boolean isRedirect = false;  
	    private String path = null;  

		public boolean isRedirect() {
			return isRedirect;
		}

		public void setRedirect(boolean isRedirect) { // 어떤 방식으로? (true : redirect, false : forward)
			this.isRedirect = isRedirect;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) { // 어디 주소로?
			this.path = path;
		}
	}